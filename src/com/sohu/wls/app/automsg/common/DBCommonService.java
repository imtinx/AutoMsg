package com.sohu.wls.app.automsg.common;

import com.sohu.wls.app.automsg.db.TaskDetailOpenHelper;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigItem;
import com.sohu.wls.app.automsg.util.DatetimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private TaskDetailOpenHelper taskDetailOpenHelper;

    public DBCommonService(UserDetailOpenHelper userDetailOpenHelper, TaskDetailOpenHelper taskDetailOpenHelper) {
        this.userDetailOpenHelper = userDetailOpenHelper;
        this.taskDetailOpenHelper = taskDetailOpenHelper;
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
         return null;
    }

    @Override
    public List<SMSTaskModel> getCurrentMonthSMSTaskDetail() {
        return taskDetailOpenHelper.queryTasks(DatetimeUtil.getCurrentYear(),DatetimeUtil.getCurrentMonth());
    }

    @Override
    public void addSMSTask(SMSTaskModel task) throws Exception {
        taskDetailOpenHelper.addTask(task);
    }

    @Override
    public void updateSMSTask(SMSTaskModel task) throws Exception {
        taskDetailOpenHelper.updateTask(task);
    }

}
