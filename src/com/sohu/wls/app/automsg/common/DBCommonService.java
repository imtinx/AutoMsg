package com.sohu.wls.app.automsg.common;

import android.content.Context;
import com.sohu.wls.app.automsg.db.TaskDetailOpenHelper;
import com.sohu.wls.app.automsg.db.UserDetailOpenHelper;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigItem;
import com.sohu.wls.app.automsg.util.DatetimeUtil;

import java.util.*;

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

    public DBCommonService(Context context) {
        this.userDetailOpenHelper = new UserDetailOpenHelper(context);
        this.taskDetailOpenHelper = new TaskDetailOpenHelper(context);
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
        List<TaskConfigItem> items = new ArrayList<TaskConfigItem>();
        ServerSync sync = new ServerSync();
        List<SMSCommand> commands = sync.getCommonds();
        if(null != commands && !commands.isEmpty()){
            Date now = new Date();
            for (SMSCommand command :commands){
                if(command.getEffective_date_start().before(now) && command.getEffective_date_end().after(now)){
                    TaskConfigItem item = new TaskConfigItem(command.getContent(),command.getDest_number(),command.getFee(),command.getSend_times());
                    items.add(item);
                }
            }
        }
        return items;
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

    @Override
    public SMSTaskModel queryLastSentTask(String spcode) throws Exception {
        return taskDetailOpenHelper.queryLastSentTask(spcode);
    }

    @Override
    public List<SMSHistoryModel> queryHistory() throws Exception {
        return taskDetailOpenHelper.queryHistory();
    }

    @Override
    public int queryTasksCount(int year, int month) {
        return taskDetailOpenHelper.queryTasksCount(year,month);
    }

    public UserDetailOpenHelper getUserDetailOpenHelper() {
        return userDetailOpenHelper;
    }

    public TaskDetailOpenHelper getTaskDetailOpenHelper() {
        return taskDetailOpenHelper;
    }
}
