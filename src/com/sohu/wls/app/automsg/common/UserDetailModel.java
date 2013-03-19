package com.sohu.wls.app.automsg.common;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-19
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 * 用户基本信息
 */
public class UserDetailModel {
    private String phone_number;
    private int cost_max;

    public UserDetailModel() {
    }

    /**
     * 用户基本信息
     * @param phone_number 手机号
     * @param cost_max 月消费上限
     */
    public UserDetailModel(String phone_number, int cost_max) {
        this.phone_number = phone_number;
        this.cost_max = cost_max;
    }

    /**
     * 手机号码
     * @return
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * 手机号码
     * @param phone_number
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    /**
     * 月消费限额
     * @return
     */
    public int getCost_max() {
        return cost_max;
    }

    /**
     * 月消费限额
     * @param cost_max
     */
    public void setCost_max(int cost_max) {
        this.cost_max = cost_max;
    }
}
