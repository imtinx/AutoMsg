package com.sohu.wls.app.automsg.tasklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.sohu.wls.app.automsg.MyActivity;
import com.sohu.wls.app.automsg.R;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;


public class TaskStatusActivity extends Activity {

    public static final int TASK_STATUS_SENT_TAG = 0;
    public static final int TASK_STATUS_REPLY_TAG = 1;
    public static final int TASK_STATUS_FINISH_TAG = 2;

    public int taskStatusExpectText = 0;
    public int taskStatusSentText = 0;
    public int taskStatusRepliedText = 0;

    public static RefreshHandler handler;

    private static TextView taskStatusExpectTextView;
    private static TextView taskStatusSentTextView;
    private static TextView taskStatusRepliedTextView;

    private static  Button sendController;
    private static ProgressBar progressBar;
    private SendSMSRunnable smsRunnable;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //初始化布局
        setContentView(R.layout.task_status);
        //初始化发送状态栏
        taskStatusExpectTextView = (TextView)findViewById(R.id.task_status_expect_text) ;
        taskStatusSentTextView = (TextView)findViewById(R.id.task_status_sent_text) ;
        taskStatusRepliedTextView = (TextView)findViewById(R.id.task_status_replied_text) ;

        //初始化发送线程
        smsRunnable = new SendSMSRunnable(this);

        taskStatusExpectText= smsRunnable.getTotalTaskNum();
        taskStatusSentText= smsRunnable.getSentTaskNum();
        taskStatusRepliedText = smsRunnable.getRepliedTaskNum();
        refreshTaskStatus();

        Thread smsThread = new Thread(smsRunnable);
        smsThread.start();


        //初始化继续/暂停按钮
        sendController = (Button) findViewById(R.id.send_controller);
        if(taskStatusExpectText==taskStatusSentText) {
            sendController.setText(R.string.task_finish);
            smsRunnable.stopSend();
            Toast.makeText(TaskStatusActivity.this, R.string.task_finish_text, Toast.LENGTH_LONG).show();
        } else{
            sendController.setText(R.string.task_start);
        }
        sendController.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button sendController = (Button) findViewById(R.id.send_controller);
                if(smsRunnable.stop){
                    Intent intent = new Intent();
                    intent.setClass(TaskStatusActivity.this, MyActivity.class);
                    startActivity(intent);
                }else{
                    if(smsRunnable.running){
                        sendController.setText(R.string.task_continue);
                        smsRunnable.pauseSend();
                    } else{
                        sendController.setText(R.string.task_pause);
                        smsRunnable.continueSend();
                    }
                }
            }
        });
        //初始化状态刷新回调
        handler = new RefreshHandler();
        //初始化进度条
        progressBar = (ProgressBar) findViewById(R.id.send_progress);
        progressBar.setMax(taskStatusExpectText);
        progressBar.setProgress(taskStatusSentText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        smsRunnable.stopSend();
        Intent intent = new Intent();
        intent.setClass(this, MyActivity.class);
        startActivity(intent);
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
                    refreshTaskStatus();
                    progressBar.incrementProgressBy(1);
                    break;
                }
                case TASK_STATUS_REPLY_TAG   :    {
                    TaskStatusActivity.this.taskStatusRepliedText++;
                    refreshTaskStatus();
                    break;
                }
                case TASK_STATUS_FINISH_TAG   :    {
                    sendController.setText(R.string.task_finish);
                    Toast.makeText(TaskStatusActivity.this, R.string.task_finish_text, Toast.LENGTH_LONG).show();
                    break;
                }

            }
        }
    }

    private void refreshTaskStatus(){
        TaskStatusActivity.this.taskStatusExpectTextView.setText(String.valueOf(taskStatusExpectText));
        TaskStatusActivity.this.taskStatusSentTextView.setText(String.valueOf(taskStatusSentText));
        TaskStatusActivity.this.taskStatusRepliedTextView.setText(String.valueOf(taskStatusRepliedText));
    }

}
