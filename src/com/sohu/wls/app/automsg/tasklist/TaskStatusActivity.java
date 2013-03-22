package com.sohu.wls.app.automsg.tasklist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.sohu.wls.app.automsg.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskStatusActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_status);
        ListView historyList = (ListView) findViewById(R.id.task_status_list);
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("click me");
            }
        }
        );
        SimpleAdapter adapter =   new SimpleAdapter(this,
                getHistoryTaskStatusList(),R.layout.task_status_list, new String[]{"task_command","task_result"},
                new int[]{R.id.task_command,R.id.task_result});

        historyList.setAdapter(adapter);
    }


    private List<Map<String, Object>> getHistoryTaskStatusList(){
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
