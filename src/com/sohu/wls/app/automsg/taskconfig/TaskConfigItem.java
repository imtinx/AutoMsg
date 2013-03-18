package com.sohu.wls.app.automsg.taskconfig;

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
    private int total;
    private int cost;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCost() {
        return this.fee*this.total;
    }


}
