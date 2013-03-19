package com.sohu.wls.app.automsg.common;

import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-19
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
public class DBCommonService implements ICommonService {

    private UserDetailOpenHelper userDetailOpenHelper;

    public DBCommonService(UserDetailOpenHelper userDetailOpenHelper) {
        this.userDetailOpenHelper = userDetailOpenHelper;
    }

    @Override
    public UserDetailModel getUserDetail() {
        try {
            return this.userDetailOpenHelper.queryUserDetailInfo();  //To change body of implemented methods use File | Settings | File Templates.
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TaskConfigItem> getSMSCommandDetail() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<SMSTaskModel> getCurrentMonthSMSTaskDetail() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
