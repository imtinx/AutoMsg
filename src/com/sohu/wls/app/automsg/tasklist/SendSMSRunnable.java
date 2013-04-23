package com.sohu.wls.app.automsg.tasklist;


import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.ICommonService;
import com.sohu.wls.app.automsg.common.SMSTaskModel;

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
    public volatile boolean running = false;
    public volatile boolean stop = false;
    private Iterator<SMSTaskModel> iterator;
    private ICommonService dbservice;
    private static final int INTERVAL = 3000; //发送频率
    public SendSMSRunnable(Context context) {
        this.dbservice= new DBCommonService(context);
        this.tasklist = dbservice.getCurrentMonthSMSTaskDetail();
        this.iterator = tasklist.iterator();
    }

    @Override
    public void run() {
        while(!stop){
            if(running){
                if(iterator.hasNext()){
                    SMSTaskModel task = iterator.next();
                    if(task.isSms_sended())
                        continue;
                    send(task);
                    task.setSms_sended(true);
                    try {
                        dbservice.updateSMSTask(task);
                        refreshTaskStatusSentText();
                    } catch (Exception e) {
                        Log.e(ACTIVITY_TAG,"update error!",e);
                    }
                    iterator.remove();
                    try {
                        Thread.currentThread().sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.e(ACTIVITY_TAG,"thread error!",e);
                    }
                }else{
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putInt("type", TaskStatusActivity.TASK_STATUS_FINISH_TAG);
                    msg.setData(b);
                    TaskStatusActivity.handler.sendMessage(msg);
                    stop = true;
                    Log.i(ACTIVITY_TAG,"mission complete!");
                }
            }
        }
    }

    /**
     * 刷新状态栏计数器
     */
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

    /**
     * 停止发送，不可继续
     */
    public void stopSend(){
        this.stop = true;
        Log.i(SendSMSRunnable.ACTIVITY_TAG,"stop sms thread!");

    }
    /**
     * 暂停发送，可以继续
     */
    public void pauseSend() {
        this.running = false;
        Log.i(SendSMSRunnable.ACTIVITY_TAG,"pause sms thread!");

    }

    /**
     * 继续发送
     */
    public void continueSend() {
        this.running = true;
        Log.i(SendSMSRunnable.ACTIVITY_TAG,"continue sms thread!");
    }

    /**
     * 获得当月发送数量总数
     * @return
     */
    public int getTotalTaskNum(){
        return tasklist.size();
    }

    /**
     * 获得当月已发送数量
     * @return
     */
    public int getSentTaskNum() {
        int i = 0;
        if (tasklist.size() != 0) {
            for (SMSTaskModel task : tasklist) {
                if (task.isSms_sended()) {
                    i++;
                }
            }
        }
        return i;
    }

    /**
     * 获得当月已回复数量
     * @return
     */
    public int getRepliedTaskNum() {
        int i = 0;
        if (tasklist.size() != 0) {
            for (SMSTaskModel task : tasklist) {
                if (task.isSms_received()) {
                    i++;
                }
            }
        }
        return i;
    }
}
