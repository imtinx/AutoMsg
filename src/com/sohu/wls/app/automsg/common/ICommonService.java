package com.sohu.wls.app.automsg.common;

import com.sohu.wls.app.automsg.taskconfig.TaskConfigItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-19
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public interface ICommonService {
    /**
     * 获取用户基本信息
     * 如果发生异常返回null
     * @return
     */
    public UserDetailModel getUserDetail();

    /**
     * 获取当月发送指令基本信息
     * @return
     */
    public List<TaskConfigItem> getSMSCommandDetail();

    /**
     * 获取当月发送任务基本信息
     * @return
     */
    public List<SMSTaskModel> getCurrentMonthSMSTaskDetail();
}
