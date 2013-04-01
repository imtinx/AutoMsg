package com.sohu.wls.app.automsg.tasklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;
import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.ICommonService;
import com.sohu.wls.app.automsg.common.SMSTaskModel;
import com.sohu.wls.app.automsg.db.TaskDetailOpenHelper;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;

/**
 * User: chaocui200783
 * Date: 13-3-30
 * Time: 下午4:30
 */
public class ReceiveSMSListener extends BroadcastReceiver {
    private static final String strACT = "android.provider.Telephony.SMS_RECEIVED";
    private static final String ACTIVITY_TAG="ReceiveSMSListener";
    private ICommonService dbservice;
    private Context context=null;


    public ReceiveSMSListener(Context context) {
        this.context= context;
        this.dbservice= new DBCommonService(new UserDetailOpenHelper(context), new TaskDetailOpenHelper(context));
    }

    public void registerAction(String action){
        IntentFilter filter=new IntentFilter();
        filter.addAction(action);
        context.registerReceiver(this, filter);
    }

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
                    try {
                        SMSTaskModel task = dbservice.queryLastSentTask(from);
                        task.setSms_received(true);
                        dbservice.updateSMSTask(task);
                    } catch (Exception e) {
                        Log.e(ACTIVITY_TAG,"update error!",e);
                    }
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