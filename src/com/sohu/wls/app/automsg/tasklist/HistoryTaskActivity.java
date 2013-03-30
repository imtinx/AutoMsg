package com.sohu.wls.app.automsg.tasklist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.sohu.wls.app.automsg.R;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.ICommonService;
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

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("task_month", "2013-11");

        String taskExpenses = getResources().getString(R.string.task_expenses);
        taskExpenses = String.format(taskExpenses,200);
        map.put("task_expenses",taskExpenses);

        String taskResult = getResources().getString(R.string.task_result);
        taskResult = String.format(taskResult, 110, 110, 110);
        map.put("task_result", taskResult);

        list.add(map);

        return list;
    }
}
