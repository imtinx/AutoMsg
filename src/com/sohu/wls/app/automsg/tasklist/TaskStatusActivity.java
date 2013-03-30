package com.sohu.wls.app.automsg.tasklist;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.sohu.wls.app.automsg.R;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.ICommonService;
import com.sohu.wls.app.automsg.common.SMSTaskModel;
import com.sohu.wls.app.automsg.db.TaskDetailOpenHelper;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskStatusActivity extends Activity {

    private volatile boolean pause = false;

    public static final int TASK_STATUS_SENT_TAG = 0;
    public static final int TASK_STATUS_REPLY_TAG = 1;


    private ICommonService dbservice = new DBCommonService(new UserDetailOpenHelper(this), new TaskDetailOpenHelper(this));


    public int taskStatusSentText = 0;
    public int taskStatusRepliedText = 0;

    public static RefreshHandler handler;

    private static TextView taskStatusExpectTextView;
    private static TextView taskStatusSentTextView;
    private static TextView taskStatusRepliedTextView;

    private SendSMSRunnable smsRunnable;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_status);
        //初始化发送状态栏
        taskStatusExpectTextView = (TextView)findViewById(R.id.task_status_expect_text) ;
        taskStatusSentTextView = (TextView)findViewById(R.id.task_status_sent_text) ;
        taskStatusRepliedTextView = (TextView)findViewById(R.id.task_status_replied_text) ;
        //初始化继续/暂停按钮
        Button sendController = (Button) findViewById(R.id.send_controller);
        sendController.setText(R.string.task_pause);

        sendController.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button sendController = (Button) findViewById(R.id.send_controller);
                if(pause){
                    sendController.setText(R.string.task_pause);
                    pause = false;
                    System.out.println("continue");
                    smsRunnable.continueSend();
                } else{
                    sendController.setText(R.string.task_continue);
                    pause = true;
                    System.out.println("pause");
                    smsRunnable.pauseSend();
                }
            }
        });
        //初始化发送列表
        ListView taskStatusList = (ListView) findViewById(R.id.task_status_list);
        taskStatusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("click me");
            }
        }
        );
        SimpleAdapter adapter =   new SimpleAdapter(this,
                getTaskStatusList(),R.layout.task_status_list, new String[]{"task_command","task_result"},
                new int[]{R.id.task_command,R.id.task_result});

        taskStatusList.setAdapter(adapter);

        List<SMSTaskModel> tasklist = dbservice.getCurrentMonthSMSTaskDetail();
        taskStatusExpectTextView.setText(String.valueOf(tasklist.size()));
        smsRunnable = new SendSMSRunnable(this,tasklist);
        Thread smsThread = new Thread(smsRunnable);
        smsThread.start();

        handler = new RefreshHandler();

    }


    class RefreshHandler extends Handler {
        public RefreshHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int type = b.getInt("type");
            switch (type)              {
                case TASK_STATUS_SENT_TAG  : {
                    TaskStatusActivity.this.taskStatusSentText++;
                    TaskStatusActivity.this.taskStatusSentTextView.setText(String.valueOf(taskStatusSentText));
                    break;
                }
                case TASK_STATUS_REPLY_TAG   :    {
                    TaskStatusActivity.this.taskStatusRepliedText++;
                    TaskStatusActivity.this.taskStatusRepliedTextView.setText(String.valueOf(taskStatusRepliedText));
                    break;
                }
            }
        }
    }



    private List<Map<String, Object>> getTaskStatusList(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        String taskCommand = getResources().getString(R.string.task_command);
        taskCommand = String.format(taskCommand, "1","1066668888");
        map.put("task_command", taskCommand);

        String taskResult = getResources().getString(R.string.task_result);
        taskResult = String.format(taskResult, 110, 110, 110);
        map.put("task_result", taskResult);

        list.add(map);

        return list;
    }
}
