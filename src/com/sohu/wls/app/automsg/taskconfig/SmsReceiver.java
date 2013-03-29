package com.sohu.wls.app.automsg.taskconfig;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.sohu.wls.app.automsg.common.SMSTaskModel;
import com.sohu.wls.app.automsg.db.TaskDetailOpenHelper;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-26
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public class SmsReceiver extends BroadcastReceiver {
    private final String TAG = "sms_receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        //To change body of implemented methods use File | Settings | File Templates.
        Log.v(TAG, ">>>>>>>onReceive start");
        // 第一步、获取短信的内容和发件人
        StringBuilder body = new StringBuilder();// 短信内容
        StringBuilder number = new StringBuilder();// 短信发件人
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] _pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] message = new SmsMessage[_pdus.length];
            for (int i = 0; i < _pdus.length; i++) {
                message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
            }
            for (SmsMessage currentMessage : message) {
                body.append(currentMessage.getDisplayMessageBody());
                if (!number.toString().equals(currentMessage.getDisplayOriginatingAddress())){
                    number.append(currentMessage.getDisplayOriginatingAddress());
                }
            }
            String smsBody = body.toString();
            String smsNumber = number.toString();
            if (smsNumber.contains("+86")) {
                smsNumber = smsNumber.substring(3);
            }
            Log.v(TAG,"receiv sms ["+smsBody+"] from ["+smsNumber+"]");

            TaskDetailOpenHelper taskDetailOpenHelper = new TaskDetailOpenHelper(context);

            try {
                SMSTaskModel task = taskDetailOpenHelper.queryLastSentTask(smsNumber);
                if (task != null){
                    task.setSms_received(true);
                    task.setRecivetime(new Date());
                    taskDetailOpenHelper.updateTask(task);
                    Intent datachangeIntent = new Intent(SmsSendActivity.DATA_CHANGE_INTENT);
                    datachangeIntent.setAction(SmsSendActivity.DATA_CHANGE_INTENT);
                    context.sendBroadcast(datachangeIntent);
                    Log.v(TAG,"broadcast custom intent["+SmsSendActivity.DATA_CHANGE_INTENT+"]");
                }

            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Log.v(TAG,e.getMessage());
            }


        }
        Log.v(TAG, ">>>>>>>onReceive end");
    }
}
