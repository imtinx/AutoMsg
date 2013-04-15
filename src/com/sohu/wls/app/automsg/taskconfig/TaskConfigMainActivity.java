package com.sohu.wls.app.automsg.taskconfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sohu.wls.app.automsg.MyActivity;
import com.sohu.wls.app.automsg.R;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.SMSTaskModel;
import com.sohu.wls.app.automsg.common.UserDetailModel;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.tasklist.TaskStatusActivity;
import com.sohu.wls.app.automsg.util.DatetimeUtil;


import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ajhlp
 * Date: 13-3-18
 * Time: 下午9:45
 * To change this template use File | Settings | File Templates.
 */
public class TaskConfigMainActivity extends Activity {
    public static final String TAG = "task_config";
    /** Called when the activity is first created. */
    private ListView taskDetailListView;
    private ConfigListAdapter adapter;
    private Button configButton;
    private AlertDialog alterConfigItemDialog;
    private AlertDialog saveConfigConfirmDialog;
    private AlertDialog saveConfigProgressDialog;
    private AlertDialog  toplimitConfirmDialog;
    private TaskConfigManageService taskConfigManageService;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_config_main);
        configButton = (Button)findViewById(R.id.toplimit_config_button) ;
        configButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog d = createToplimitConfirmDialog(TaskConfigMainActivity.this);

                d.show();

                EditText topcost_field = (EditText) d.findViewById(R.id.toplimit_config_item_edit);

                String topcost = "" + getUserDetailTopCost();

                topcost_field.setText(topcost);
                topcost_field.setSelection(topcost_field.getText().toString().length());
            }
            });
        taskDetailListView = (ListView)findViewById(R.id.task_config_main_detaillistview);
        taskConfigManageService = new TaskConfigManageService(new DBCommonService(this),this);
        List<TaskConfigItem> configs = taskConfigManageService.initTasks();
        Log.v(TAG,"view create! init task size: "+ configs.size());
        adapter = new ConfigListAdapter(this,configs);
        taskDetailListView.setAdapter(adapter);
        taskDetailListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int position, long rowid) {
                // TODO Auto-generated method stub


                AlertDialog d = createAddConfigDialog(TaskConfigMainActivity.this);
                d.show();
                TextView contentField = (TextView) d.findViewById(R.id.task_config_item_edit_content);
                TextView spcodeField = (TextView) d.findViewById(R.id.task_config_item_edit_spcode);
                TextView feeField = (TextView) d.findViewById(R.id.task_config_item_edit_fee);
                EditText countField = (EditText) d.findViewById(R.id.task_config_item_edit_count);

                TaskConfigItem configModel = adapter.getConfigs().get(position);

                contentField.setText(configModel.getContent());
                spcodeField.setText(configModel.getSpcode());
                feeField.setText(configModel.getFee()+"");

                countField.setText(configModel.getTotal()+"");
                countField.setSelection(countField.getText().toString().length());

                return true;
            }

        });
        updateSummaryInfo();

        if(getUserDetailTopCost() == 0){
            Toast.makeText(this, "请点击【修改限额】配置月最高消费额度", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取用户消费限额
     * @return
     */
    public int getUserDetailTopCost(){
        UserDetailOpenHelper userDetailOpenHelper = ((DBCommonService)taskConfigManageService.getCommonService()).getUserDetailOpenHelper();

        try {
            UserDetailModel userDetailModel = userDetailOpenHelper.queryUserDetailInfo();
            return userDetailModel.getCost_max();
        } catch (Exception e) {
            Log.w(TAG,e);
            return 0;
        }

    }

    /**
     * 更新发送概况信息
     */
    public void updateSummaryInfo(){
        TextView costField = (TextView) findViewById(R.id.task_config_main_cost);
        TextView totalField = (TextView) findViewById(R.id.task_config_main_total);

        costField.setText(taskConfigManageService.getTotalSendCost()+"");
        totalField.setText(taskConfigManageService.getTotalSendNumber()+"");
    }




    /**
     * 点击确定按钮
     * @param view
     */
    public void onSureButtonClick(View view){
        TextView totalField = (TextView) findViewById(R.id.task_config_main_total);
        Log.v(TAG,"activity:"+(TaskConfigMainActivity.this));

        if(adapter.getConfigs() != null && adapter.getConfigs().size()>0
                && !totalField.getText().toString().equals("0")){

            AlertDialog dialog = createSaveConfirmDialog(TaskConfigMainActivity.this);
            String msg = "";
            if ((taskConfigManageService.getCurrentMonthSavedTaskCost()+
                    taskConfigManageService.getTotalSendCost())>getUserDetailTopCost())  {
                msg += "本月费用已超过消费限额，";
            }
            dialog.setMessage(msg+"确认保存配置？");
            dialog.show();
        }else{
            //如果任务都发送完毕，保持在该界面
            if (taskConfigManageService.isCurrentMonthTaskDone()){
                Toast.makeText(this, R.string.task_config_send_none_msg, Toast.LENGTH_LONG).show();
            //跳转发送界面
            }else{
                Intent intent = new Intent();
                intent.setClass(TaskConfigMainActivity.this, TaskStatusActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 保存任务线程，防止主界面卡顿，保存完毕后清除主界面遮罩
     */
    class SaveTaskThread implements Runnable{
        private Context context;
        SaveTaskThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {

            int year = DatetimeUtil.getCurrentYear();
            int month = DatetimeUtil.getCurrentMonth();
            int sendedtaskcount = taskConfigManageService.getCommonService().getCurrentMonthSMSTaskDetail().size();
            for (TaskConfigItem item : adapter.getConfigs()){
                for (String id : item.getIds(sendedtaskcount)){
                    SMSTaskModel task = new SMSTaskModel(id,item.getContent(),item.getSpcode(),item.getFee(),year,month);
                    try {
                        taskConfigManageService.getCommonService().addSMSTask(task);
                    } catch (Exception e) {
                        Log.w(TAG, "添加任务["+id+"]失败");
                    }
                }
            }
            createSaveProgressDialog(context).dismiss();
            Intent intent = new Intent();
            intent.setClass(context, TaskStatusActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 返回按键，返回主界面
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, MyActivity.class);
        startActivity(intent);
    }
    /**
     * 修改限额确认对话框
     * @param context
     * @return
     */
    public AlertDialog createToplimitConfirmDialog(final Context context){

        if (toplimitConfirmDialog != null){
            return toplimitConfirmDialog;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("修改发送限额");
        builder.setView(inflater.inflate(R.layout.toplimit_config_item, null)) ;


        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonid) {
                AlertDialog d = (AlertDialog)dialog;
                EditText topcost_field = (EditText) d.findViewById(R.id.toplimit_config_item_edit);
                //保存发送限额
                String val_edit = topcost_field.getText().toString();
                if (val_edit == null || "".equals(val_edit)){
                    Toast.makeText(TaskConfigMainActivity.this, R.string.user_detail_cost_max_empty_msg, Toast.LENGTH_LONG).show();
                    return;
                }

                UserDetailModel userDetailModel = new UserDetailModel();
                userDetailModel.setPhone_number("");
                userDetailModel.setCost_max(Integer.parseInt(val_edit));

                try {
                    UserDetailOpenHelper userDetailOpenHelper = ((DBCommonService)taskConfigManageService.getCommonService()).getUserDetailOpenHelper();
                    userDetailOpenHelper.updateUserDetail(userDetailModel);
                    Toast.makeText(TaskConfigMainActivity.this, R.string.user_detail_save_success_msg, Toast.LENGTH_SHORT).show();
                    if (!taskConfigManageService.isCurrentMonthHasTask()){
                        List<TaskConfigItem> configs = adapter.getConfigs();
                        taskConfigManageService.initTasks();
                        Map<String,TaskConfigItem> new_configs = taskConfigManageService.getTasks();

                        for (TaskConfigItem config : configs){
                            if (new_configs.containsKey(taskConfigManageService.generateKey(config))){
                                config.setTotal(new_configs.get(taskConfigManageService.generateKey(config)).getTotal());
                            }
                        }

                        adapter.notifyDataSetChanged();
                        updateSummaryInfo();
                    }
                } catch (Exception e) {
                    Toast.makeText(TaskConfigMainActivity.this, R.string.user_detail_save_fail_msg+"["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
                }

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        toplimitConfirmDialog = builder.create();


        return toplimitConfirmDialog;
    }
    /**
     * 保存任务确认对话框
     * @param context
     * @return
     */
    public AlertDialog createSaveConfirmDialog(final Context context){

        if (saveConfigConfirmDialog != null){
            return saveConfigConfirmDialog;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("提醒");


        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int buttonid) {
                // TODO Auto-generated method stub
               createSaveProgressDialog(context).show();
               Thread t = new Thread(new SaveTaskThread(context)) ;

                t.start();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub

                dialog.dismiss();
            }
        });

        saveConfigConfirmDialog = builder.create();
        return saveConfigConfirmDialog;
    }

    /**
     * 保存任务进度对话框
     * @param context
     * @return
     */
    public AlertDialog createSaveProgressDialog(Context context){

        if (saveConfigProgressDialog != null){
            return saveConfigProgressDialog;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        builder.setView(inflater.inflate(R.layout.task_config_save_progress, null));
        builder.setCancelable(false);

        saveConfigProgressDialog = builder.create();
        return saveConfigProgressDialog;
    }

    /**
     * 生成编辑发送详情窗口
     * @param context
     * @return
     */
    public AlertDialog createAddConfigDialog(final Context context){
        if(alterConfigItemDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle(R.string.task_config_edit_name);
            LayoutInflater inflater = LayoutInflater.from(context);
            builder.setView(inflater.inflate(R.layout.task_config_item_edit, null));
            builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int buttonid) {
                    // TODO Auto-generated method stub
                    AlertDialog d = (AlertDialog)dialog;
                    TextView contentField = (TextView) d.findViewById(R.id.task_config_item_edit_content);
                    TextView spcodeField = (TextView) d.findViewById(R.id.task_config_item_edit_spcode);
                    EditText countField = (EditText) d.findViewById(R.id.task_config_item_edit_count);



                    if(countField.getText().toString().trim().equals("")){
                        return;
                    }


                    TaskConfigItem configModel = new TaskConfigItem();
                    configModel.setContent(contentField.getText().toString());
                    configModel.setSpcode(spcodeField.getText().toString());
                    configModel.setTotal(Integer.parseInt(countField.getText().toString()));
                    if(taskConfigManageService.editTaskDetail(configModel)){
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                        updateSummaryInfo();
                    }else{
                        Toast.makeText(context, R.string.task_config_edit_fail_msg, Toast.LENGTH_LONG).show();
                    }
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int arg1) {
                    // TODO Auto-generated method stub

                    dialog.dismiss();
                }
            });

            alterConfigItemDialog = builder.create();
        }

        return alterConfigItemDialog;
    }


    class ConfigListAdapter extends BaseAdapter {

        private List<TaskConfigItem> configs;

        private Context context;

        public ConfigListAdapter(Context context, List<TaskConfigItem> configs){
            setContext(context);
            setConfigs(configs);
        }



        @Override
        public void notifyDataSetChanged() {
            // TODO Auto-generated method stub
            super.notifyDataSetChanged();

        }



        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return configs == null ? 0 : configs.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return configs == null ? null : configs.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup group) {
            // TODO Auto-generated method stub
            ConfigListItemHolder holder = null;
            if(view == null){
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.task_config_detail_list_item, null);

                holder = new ConfigListItemHolder();
                holder.spcode = (TextView) view.findViewById(R.id.task_config_item_spcode);
                holder.content = (TextView) view.findViewById(R.id.task_config_item_content);
                holder.fee = (TextView) view.findViewById(R.id.task_config_item_fee);
                holder.total = (TextView) view.findViewById(R.id.task_config_item_total);
                view.setTag(holder);
            }else{
                holder = (ConfigListItemHolder) view.getTag();
            }

            holder.spcode.setText(configs.get(position).getSpcode());
            holder.content.setText(configs.get(position).getContent());
            holder.fee.setText(configs.get(position).getFee()+"");
            holder.total.setText(configs.get(position).getTotal()+"");


            return view;
        }

        public List<TaskConfigItem> getConfigs() {
            return configs;
        }

        public void setConfigs(List<TaskConfigItem> configs) {
            this.configs = configs;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

    }

    class ConfigListItemHolder {
        public TextView spcode;
        public TextView content;
        public TextView fee;
        public TextView total;
    }
}
