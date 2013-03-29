package com.sohu.wls.app.automsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;
import com.sohu.wls.app.automsg.tasklist.TaskStatusActivity;


public class MyActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


    }

    public void onItemClick(View view){
        Intent intent = new Intent();
        if (view.getId() == R.id.nav_user_detail){
            intent.setClass(this,UserDetailManageActivity.class);
        }else if (view.getId() == R.id.nav_task_config){
            intent.setClass(this, TaskConfigMainActivity.class);
        }else if (view.getId() == R.id.nav_task_status){
            intent.setClass(this, TaskStatusActivity.class);
        }
        startActivity(intent);
    }


}
