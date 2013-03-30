package com.sohu.wls.app.automsg.tasklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * User: chaocui200783
 * Date: 13-3-30
 * Time: 下午4:30
 */
public class ReceiveSMSListener extends BroadcastReceiver {
    private static final String strACT = "android.provider.Telephony.SMS_RECEIVED";
    private static final String ACTIVITY_TAG="ReceiveSMSListener";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(strACT)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs;
                msgs = new SmsMessage[pdus.length];
                for (int i=0; i<msgs.length; i++){
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    String from = msgs[i].getDisplayOriginatingAddress();
                    String content = msgs[i].getDisplayMessageBody();
                    refreshTaskStatusReplyText();
                    Log.i(ACTIVITY_TAG,"receive sms:{from:"+from+",content:"+content+"}");
                }
            }
        }

    }
    private void refreshTaskStatusReplyText(){
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putInt("type", TaskStatusActivity.TASK_STATUS_REPLY_TAG);
        msg.setData(b);
        TaskStatusActivity.handler.sendMessage(msg);
    }

}