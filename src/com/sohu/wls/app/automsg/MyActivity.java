package com.sohu.wls.app.automsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigManageService;
import com.sohu.wls.app.automsg.tasklist.HistoryTaskActivity;
import com.sohu.wls.app.automsg.tasklist.TaskStatusActivity;



public class MyActivity extends Activity {

    private TaskConfigManageService taskConfigManageService;
    private UserDetailOpenHelper userDetailOpenHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        taskConfigManageService = new TaskConfigManageService(new DBCommonService(this),this);
        if (userDetailOpenHelper == null){
            userDetailOpenHelper = new UserDetailOpenHelper(this);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Dialog dialog = new AlertDialog.Builder(MyActivity.this)
                .setTitle("信息提示")
                .setMessage("确定要退出系统吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .create();
        dialog.show();
    }

    public void onItemClick(View view){
        boolean taskDone = taskConfigManageService.isCurrentMonthTaskDone();
        if (view.getId() == R.id.start_button) {
            if(taskDone){
                startActivity(new Intent(MyActivity.this, TaskConfigMainActivity.class));
            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setTitle("上次发送任务尚未完成");
                builder.setPositiveButton("继续发送", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        startActivity(new Intent(MyActivity.this,TaskStatusActivity.class));
                    }
                });
                builder.setNegativeButton("追加任务", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        startActivity(new Intent(MyActivity.this, TaskConfigMainActivity.class));
                    }
                });
                builder.show();
            }
        } else if (view.getId() == R.id.history_button) {
            startActivity(new Intent(MyActivity.this, HistoryTaskActivity.class));
        }
    }

}
