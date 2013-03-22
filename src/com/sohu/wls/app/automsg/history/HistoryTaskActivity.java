package com.sohu.wls.app.automsg.history;

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

/**
 *历史任务状态
 */
public class HistoryTaskActivity extends Activity {

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
                getHistoryTaskList(),R.layout.history_task_list, new String[]{"history_month","history_expenses","history_result"},
                new int[]{R.id.history_month,R.id.history_expenses,R.id.history_result});

        historyList.setAdapter(adapter);
    }

    /**
     * 获取历史任务数据
     * @return   历史任务数据
     */
    private List<Map<String, Object>> getHistoryTaskList(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("history_month", "2013-11");

        String historyExpenses = getResources().getString(R.string.history_expenses);
        historyExpenses = String.format(historyExpenses,200);
        map.put("history_expenses",historyExpenses);

        String historyResult = getResources().getString(R.string.history_result);
        historyResult = String.format(historyResult, 110, 110, 110);
        map.put("history_result", historyResult);

        list.add(map);

        return list;
    }
}
