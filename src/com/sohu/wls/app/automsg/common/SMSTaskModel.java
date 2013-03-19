package com.sohu.wls.app.automsg.common;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-19
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 * 发送任务
 */
public class SMSTaskModel {
    private String task_id;
    private String sms_content;
    private String sms_destnumber;
    private boolean sms_sended;
    private boolean sms_received;
    private int year;
    private int month;
    private Date starttime;
    private Date recivetime;

    /**
     * 任务ID
     * spcode-content-yyyymm-sn
     * @return
     */
    public String getTask_id() {
        return task_id;
    }

    /**
     * 短信发送内容
     * @return
     */
    public String getSms_content() {
        return sms_content;
    }

    /**
     * 发送短信目的号码
     * @return
     */
    public String getSms_destnumber() {
        return sms_destnumber;
    }

    /**
     * 是否已发送
     * @return
     */
    public boolean isSms_sended() {
        return sms_sended;
    }

    /**
     * 是否已发送
     * @param sms_sended
     */
    public void setSms_sended(boolean sms_sended) {
        this.sms_sended = sms_sended;
    }

    /**
     * 是否已接受
     * @return
     */
    public boolean isSms_received() {
        return sms_received;
    }

    /**
     * 是否已接受
     * @param sms_received
     */
    public void setSms_received(boolean sms_received) {
        this.sms_received = sms_received;
    }

    /**
     * 发送日期的年份
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * 发送日期的月份
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     * 发送开始时间
     * @return
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 发送开始时间
     * @param starttime
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 收到回复时间
     * @return
     */
    public Date getRecivetime() {
        return recivetime;
    }

    /**
     * 收到回复时间
     * @param recivetime
     */
    public void setRecivetime(Date recivetime) {
        this.recivetime = recivetime;
    }
}
