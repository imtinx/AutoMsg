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

    /**
     * 增加任务
     * @param task
     */
    public void addSMSTask(SMSTaskModel task) throws Exception;

    /**
     * 更新任务状态
     * task_id为必须值
     * @param task
     */
    public void updateSMSTask(SMSTaskModel task) throws Exception;

    /**
     * 根据收到短信的号码，查找最早的未收到回复的发送任务
     * @param spcode
     * @return
     */
    public SMSTaskModel queryLastSentTask(String spcode)  throws Exception;
    /**
     * 获得历史统计数据
     * @return
     * @throws Exception
     */
    public List<SMSHistoryModel> queryHistory() throws Exception;
    /**
     * 查询某月的任务总数
     * @param year  指定年份
     * @param month 指定月份
     * @return
     */
    public int queryTasksCount(int year, int month);

    }
