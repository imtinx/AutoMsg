package com.sohu.wls.app.automsg.tasklist;


import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.ICommonService;
import com.sohu.wls.app.automsg.common.SMSTaskModel;
import com.sohu.wls.app.automsg.db.TaskDetailOpenHelper;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: chaocui200783
 * Date: 13-3-28
 * Time: 下午4:05
 */
public class SendSMSRunnable implements Runnable {

    private static final String ACTIVITY_TAG="SendSMSRunnable";

    private List<SMSTaskModel> tasklist;
    private Context context;
    private volatile boolean running = true;
    public SendSMSRunnable(Context context,List<SMSTaskModel> tasklist) {
        this.context = context;
        this.tasklist = tasklist;
    }

    @Override
    public void run() {
        Iterator<SMSTaskModel> iter  =tasklist.iterator();

        while (true) {
            while (iter.hasNext()) {
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (running) {
                    System.out.println("sending");
                    SMSTaskModel task = iter.next();
                    send(task);
                    task.setSms_sended(true);
                    ICommonService dbservice = new DBCommonService(new UserDetailOpenHelper(context), new TaskDetailOpenHelper(context));
                    try {
                        dbservice.updateSMSTask(task);
                        refreshTaskStatusSentText();
                    } catch (Exception e) {
                        //更新失败
                        e.printStackTrace();
                    }
                    iter.remove();
                }
            }
        }
    }

    private void refreshTaskStatusSentText(){
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putInt("type", TaskStatusActivity.TASK_STATUS_SENT_TAG);
        msg.setData(b);
        TaskStatusActivity.handler.sendMessage(msg);
    }


    /**
     * 发送短信
     * @param task
     * @return
     */
    private void send(SMSTaskModel task){
        SmsManager manager=SmsManager.getDefault();
        ArrayList<String> texts = manager.divideMessage(task.getSms_content());
        for(String text:texts){
            manager.sendTextMessage(task.getSms_destnumber(),null, text,null,null);
            Log.i(SendSMSRunnable.ACTIVITY_TAG,"send sms:{mobile:"+task.getSms_destnumber()+",text:"+text+"}");
        }
    }

    public void setTasklist(List<SMSTaskModel> tasklist) {
        this.tasklist = tasklist;
    }

    public void pauseSend() {
        this.running = false;
    }

    public void continueSend() {
        this.running = true;
    }
}
