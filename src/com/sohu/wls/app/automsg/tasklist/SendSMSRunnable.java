package com.sohu.wls.app.automsg.tasklist;

import com.sohu.wls.app.automsg.common.DBCommonService;
import com.sohu.wls.app.automsg.common.ICommonService;
import com.sohu.wls.app.automsg.common.SMSTaskModel;

import java.util.List;

/**
 * User: chaocui200783
 * Date: 13-3-28
 * Time: 下午4:05
 */
public class SendSMSRunnable implements Runnable {

    List<SMSTaskModel> tasklist;

    public SendSMSRunnable(List<SMSTaskModel> tasklist) {
        this.tasklist = tasklist;
    }

    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
