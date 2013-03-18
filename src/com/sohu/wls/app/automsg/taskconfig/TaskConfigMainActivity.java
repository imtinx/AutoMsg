package com.sohu.wls.app.automsg.taskconfig;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ajhlp
 * Date: 13-3-18
 * Time: 下午9:45
 * To change this template use File | Settings | File Templates.
 */
public class TaskConfigMainActivity extends Activity {

    /** Called when the activity is first created. */
    private ListView taskDetailListView;
    private ConfigListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_config_main);

        taskDetailListView = (ListView)findViewById(R.id.task_config_main_detaillistview);
        List<TaskConfigItem> configs = new ArrayList<TaskConfigItem>();
        Random random = new Random();

        for(int i=0;i<20;i++){

            TaskConfigItem model = new TaskConfigItem();
            model.setContent((i+1)+"");
            model.setSpcode("10666666"+i);
            model.setFee(random.nextInt(2)+1);
            model.setTotal(random.nextInt(20) + 1);


            configs.add(model);
        }


        adapter = new ConfigListAdapter(this,configs);
        taskDetailListView.setAdapter(adapter);

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
                view = layoutInflater.inflate(R.layout.task_config_detaillist, null);

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
