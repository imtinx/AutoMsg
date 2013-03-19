package com.sohu.wls.app.automsg.taskconfig;

import android.util.Log;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-19
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */
public class TaskConfigManageService {
    /**
     * 已发送内容+发送目的号码为key
     */
    private static Map<String,TaskConfigItem> tasks ;
    private static List<TaskConfigItem> taskList;

    public TaskConfigManageService() {
        if (tasks == null){
            tasks = new HashMap<String, TaskConfigItem>();
        }
        if (taskList == null){
            taskList = new ArrayList<TaskConfigItem>();
        }
    }

    /**
     * 生成key
     * content+"-"+spcode
     * @param model
     * @return
     */
    public String generateKey(TaskConfigItem model){
        return model.getContent()+"-"+model.getSpcode();
    }

    /**
     * 初始化任务列表
     * @return
     */
    public List<TaskConfigItem> initTasks(){
        Random random = new Random();

        for(int i=0;i<20;i++){

            TaskConfigItem model = new TaskConfigItem();
            model.setContent((i + 1) + "");
            model.setSpcode("10666666" + i);
            model.setFee(random.nextInt(2) + 1);
            model.setTotal(random.nextInt(20) + 1);

            tasks.put(generateKey(model),model);
        }

        taskList.clear();
        taskList.addAll(tasks.values());
        return taskList;
    }

    /**
     * 修改任务发送数量
     * @param model
     */
    public void editTaskDetail(TaskConfigItem model){
        Log.i("automsg", generateKey(model) + "  ->  " + model.getTotal());
        if(tasks.get(generateKey(model)) != null &&
                model.getTotal()>=0){

            tasks.get(generateKey(model)).setTotal(model.getTotal());
        }

    }

    /**
     * 获取总发送数量
     * @return
     */
    public int getTotalSendNumber(){
        int sum = 0;

        Iterator<TaskConfigItem> models = tasks.values().iterator();

        while (models.hasNext()){
            sum += models.next().getTotal();
        }

        return sum;
    }

    /**
     * 获取总消费金额
     * @return
     */
    public int getTotalSendCost(){
        int sum = 0;

        Iterator<TaskConfigItem> models = tasks.values().iterator();

        while (models.hasNext()){
            sum += models.next().getCost();
        }

        return sum;
    }
}
