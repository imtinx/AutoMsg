package com.sohu.wls.app.automsg.taskconfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ajhlp
 * Date: 13-3-18
 * Time: 下午9:50
 * To change this template use File | Settings | File Templates.
 */
public class TaskConfigItem {
    private String content;
    private String spcode;
    private int fee;
    private int total_max;
    private int total;
    private int cost;
    private List<String> ids;

    public TaskConfigItem() {
    }

    public TaskConfigItem(String content, String spcode, int fee, int total_max) {
        this.content = content;
        this.spcode = spcode;
        this.fee = fee;
        this.total_max = total_max;
    }

    /**
     * 发送短信内容
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * 发送短信内容
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 发送目的号码
     * @return
     */
    public String getSpcode() {
        return spcode;
    }

    /**
     * 发送目的号码
     * @param spcode
     */
    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    /**
     * 单条短信费用，单位：元
     * @return
     */
    public int getFee() {
        return fee;
    }

    /**
     * 单条短信费用，单位：元
     * @param fee
     */
    public void setFee(int fee) {
        this.fee = fee;
    }

    /**
     * 最多发送条数
     * @return
     */
    public int getTotal_max() {
        return total_max;
    }

    /**
     * 最多发送条数
     * @param total_max
     */
    public void setTotal_max(int total_max) {
        this.total_max = total_max;
    }

    /**
     * 总发送条数
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * 总发送条数
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 总消费金额
     * @return
     */
    public int getCost() {
        return this.fee*this.total;
    }

    /**
     * 生成任务的ID
     * spcode-content-yyyymm-sn
     * @return
     */
    public List<String> getIds() {
        if(this.ids == null){
            this.ids = new ArrayList<String>();
        }
        this.ids.clear();
        String date_part = new SimpleDateFormat("yyyyMM").format(new Date());
        for (int i=0;i<getTotal();i++){
            this.ids.add(getSpcode()+"-"+getContent()+"-"+date_part+"-"+(i+1));
        }
        return ids;
    }
}
