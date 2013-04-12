package com.sohu.wls.app.automsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.sohu.wls.app.automsg.common.*;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigManageService;
import com.sohu.wls.app.automsg.tasklist.HistoryTaskActivity;
import com.sohu.wls.app.automsg.tasklist.TaskStatusActivity;

import java.util.List;


public class MyActivity extends Activity {

    private TaskConfigManageService taskConfigManageService;
    private UserDetailOpenHelper userDetailOpenHelper;
    private static final String ACTIVITY_TAG="MyActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        taskConfigManageService = new TaskConfigManageService(new DBCommonService(this),this);
        if (userDetailOpenHelper == null){
            userDetailOpenHelper = new UserDetailOpenHelper(this);
        }
        UserDetailModel detailModel = null;
        try {
            detailModel = userDetailOpenHelper.queryUserDetailInfo();
        } catch (Exception e) {
            Log.e(ACTIVITY_TAG,"load user detail error",e);
            e.printStackTrace();
        }
        if (detailModel == null || detailModel.getPhone_number() == null || "".equals(detailModel.getPhone_number())) {
            Intent intent = new Intent();
            intent.setClass(this,UserDetailManageActivity.class);
            startActivity(intent);
        } else{

        }
    }

    public void onItemClick(View view){
        Intent intent = new Intent();
        if (view.getId() == R.id.button_config){
            intent.setClass(this,UserDetailManageActivity.class);
        }else if (view.getId() == R.id.button_guide){
            if (isConfigSaved()){
                intent.setClass(this, TaskStatusActivity.class);
            }else{
                intent.setClass(this, TaskConfigMainActivity.class);
            }
        }else if (view.getId() == R.id.button_history){
            intent.setClass(this, HistoryTaskActivity.class);
        }
        startActivity(intent);
    }

    public boolean isConfigSaved(){
        List<SMSTaskModel> tasks =  taskConfigManageService.getCommonService().getCurrentMonthSMSTaskDetail();
        if (tasks.size()>0){
            return true;
        }
        return false;
    }


}
