package com.sohu.wls.app.automsg.tasklist;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.sohu.wls.app.automsg.R;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.ICommonService;
import com.sohu.wls.app.automsg.common.SMSHistoryModel;
import com.sohu.wls.app.automsg.db.TaskDetailOpenHelper;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *历史任务状态
 */
public class HistoryTaskActivity extends Activity {

    private ICommonService dbservice = new DBCommonService(new UserDetailOpenHelper(this), new TaskDetailOpenHelper(this));
    private static final String ACTIVITY_TAG="HistoryTaskActivity";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.history_task);
        ListView historyList = (ListView) findViewById(R.id.history_task_list);
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("click me");
            }
        }
        );
        SimpleAdapter adapter =   new SimpleAdapter(this,
                getHistoryTaskList(),R.layout.history_task_list, new String[]{"task_month","task_expenses","task_result"},
                new int[]{R.id.task_month,R.id.task_expenses,R.id.task_result});

        historyList.setAdapter(adapter);
    }

    /**
     * 获取历史任务数据
     * @return   历史任务数据
     */
    private List<Map<String, Object>> getHistoryTaskList(){
        List<SMSHistoryModel> historyModels;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            historyModels = dbservice.queryHistory();
            if(historyModels!=null&&historyModels.size()!=0) {
                for(SMSHistoryModel historyModel : historyModels){
                    int count = dbservice.queryTasksCount(historyModel.getYear(),historyModel.getMonth());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("task_month", historyModel.getYear()+"-"+historyModel.getMonth());

                    String taskExpenses = getResources().getString(R.string.task_expenses);
                    taskExpenses = String.format(taskExpenses,historyModel.getFee());
                    map.put("task_expenses",taskExpenses);

                    String taskResult = getResources().getString(R.string.task_result);
                    taskResult = String.format(taskResult, count, historyModel.getSentNum(), historyModel.getRepliedNum());
                    map.put("task_result", taskResult);

                    list.add(map);
                }
            }

        } catch (Exception e) {
            Log.e(ACTIVITY_TAG,"Get history error!",e);
        }
        return list;
    }
}
