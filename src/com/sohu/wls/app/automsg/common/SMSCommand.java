package com.sohu.wls.app.automsg.common;

import java.util.Date;

/**
 * User: tinx
 * Date: 13-4-9
 * Time: 下午4:00
 */
public class SMSCommand {
    //短信息目标号码
    private String dest_number;
    //短信内容
    private String content;
    //费率
    private int fee;
    //指令有效地开始时间
    private Date effective_date_start;
   //指令有效的结束时间
    private Date effective_date_end;
    //要求发送的次数
    private int send_times;

    public String getDest_number() {
        return dest_number;
    }

    public void setDest_number(String dest_number) {
        this.dest_number = dest_number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public Date getEffective_date_start() {
        return effective_date_start;
    }

    public void setEffective_date_start(Date effective_date_start) {
        this.effective_date_start = effective_date_start;
    }

    public Date getEffective_date_end() {
        return effective_date_end;
    }

    public void setEffective_date_end(Date effective_date_end) {
        this.effective_date_end = effective_date_end;
    }

    public int getSend_times() {
        return send_times;
    }

    public void setSend_times(int send_times) {
        this.send_times = send_times;
    }

    @Override
    public String toString() {
        return "SMSCommand{" +
                "dest_number='" + dest_number + '\'' +
                ", content='" + content + '\'' +
                ", fee=" + fee +
                ", effective_date_start=" + effective_date_start +
                ", effective_date_end=" + effective_date_end +
                ", send_times=" + send_times +
                '}';
    }
}
