package com.sohu.wls.app.automsg.tasklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
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

    @Override
    public void onReceive(Context context, Intent intent) {
        this.dbservice= new DBCommonService(context);
        if (intent.getAction().equals(strACT)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs;
                msgs = new SmsMessage[pdus.length];
                String from=null;
                String content=null;
                for (int i=0; i<msgs.length; i++){
                    try {
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    from = msgs[i].getDisplayOriginatingAddress();

                    if(from.startsWith("86"))
                        from = from.substring(2);
                    if(from.startsWith("+86"))
                        from = from.substring(3);

                        content = msgs[i].getDisplayMessageBody();
                        SMSTaskModel task = dbservice.queryLastSentTask(from);
                        if (task ==null)
                           continue;
                        task.setSms_received(true);
                        dbservice.updateSMSTask(task);
                        refreshTaskStatusReplyText();
                   } catch (Exception e) {
                        Log.e(ACTIVITY_TAG,"update error!",e);
                    }
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