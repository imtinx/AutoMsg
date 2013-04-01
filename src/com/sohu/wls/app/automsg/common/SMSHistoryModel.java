package com.sohu.wls.app.automsg.common;

/**
 * User: chaocui200783
 * Date: 13-4-1
 * Time: 下午2:54
 * 历史统计
 */
public class SMSHistoryModel {

    private int year;
    private int month;
    private int sentNum;
    private int repliedNum;
    private int fee;

    public SMSHistoryModel(int year, int month, int sentNum, int repliedNum, int fee) {
        this.year = year;
        this.month = month;
        this.sentNum = sentNum;
        this.repliedNum = repliedNum;
        this.fee = fee;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getSentNum() {
        return sentNum;
    }

    public void setSentNum(int sentNum) {
        this.sentNum = sentNum;
    }

    public int getRepliedNum() {
        return repliedNum;
    }

    public void setRepliedNum(int repliedNum) {
        this.repliedNum = repliedNum;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "SMSHistoryModel{" +
                "year=" + year +
                ", month=" + month +
                ", sentNum=" + sentNum +
                ", repliedNum=" + repliedNum +
                ", fee=" + fee +
                '}';
    }
}
