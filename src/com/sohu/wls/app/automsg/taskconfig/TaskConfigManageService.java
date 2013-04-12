package com.sohu.wls.app.automsg.taskconfig;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.sohu.wls.app.automsg.common.ICommonService;
import com.sohu.wls.app.automsg.common.UserDetailModel;

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
    private ICommonService commonService;
    private Activity activity;
    public TaskConfigManageService(ICommonService commonService, Activity activity) {
        if (tasks == null){
            tasks = new HashMap<String, TaskConfigItem>();
        }
        if (taskList == null){
            taskList = new ArrayList<TaskConfigItem>();
        }
        this.commonService = commonService;
        this.activity = activity;
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

    public List<TaskConfigItem> fetchOriginalTaskItems(){

        List<TaskConfigItem> items = new ArrayList<TaskConfigItem>();

//        items.add(new TaskConfigItem("1","1066666611",1,20));
//        items.add(new TaskConfigItem("19","10661156",1,20));
//        items.add(new TaskConfigItem("4","10661156",1,20));
//        items.add(new TaskConfigItem("B1","106611566",1,20));
//        items.add(new TaskConfigItem("5","1066666675",2,10));
//        items.add(new TaskConfigItem("13","1066666678",2,10));
//        items.add(new TaskConfigItem("21","1066666619",2,10));
//        items.add(new TaskConfigItem("5","1066666695",2,10));
        items.add(new TaskConfigItem("cxye","10086",2,10));
        items.add(new TaskConfigItem("cxgll","10086",1,10));
        items.add(new TaskConfigItem("CXBX","10086",1,10));
        items.add(new TaskConfigItem("CXGFX","10086",2,10));
        items.add(new TaskConfigItem("CXCCT","10086",2,10));
        items.add(new TaskConfigItem("1","1066666611",1,1));

        return items;
    }

    /**
     * 初始化任务列表
     * @return
     */
    public List<TaskConfigItem> initTasks(){

        tasks.clear();
        taskList.clear();
        UserDetailModel detailModel = commonService.getUserDetail();
        if (detailModel == null){
            return taskList;
        }
        if (detailModel.getCost_max() == 0){
            Toast.makeText(activity,"目前消费限额设置为0",Toast.LENGTH_LONG);
            return taskList;
        }
        int cost_max = commonService.getUserDetail().getCost_max();
        //服务器端获取的发送信息
        List<TaskConfigItem> originalTasks = commonService.getSMSCommandDetail();

        if(originalTasks == null || originalTasks.isEmpty()){
            return  taskList;
        }
        List<String> keys_cycle = new ArrayList<String>();
        for(TaskConfigItem item : originalTasks){
            keys_cycle.add(generateKey(item));
        }
        while(true){
            //逐条增加发送数量
            for(int i=0;i<originalTasks.size();i++){
                TaskConfigItem item = originalTasks.get(i);
                if (!keys_cycle.contains(generateKey(item))){
                    continue;
                }
                if(!tasks.containsKey(generateKey(item))){
                    tasks.put(generateKey(item),item);
                }

                //当总消费为负时，不在增加发送数量
                if(cost_max-item.getFee()<0){
                    keys_cycle.remove(generateKey(item));
                    continue;
                }
                if (item.getTotal_max()>=item.getTotal()+1){
                    item.setTotal(item.getTotal()+1);
                    cost_max -= item.getFee();
                }else{
                   keys_cycle.remove(generateKey(item));
                }

                if(keys_cycle.size()==0){
                    break;
                }
            }
            if(keys_cycle.size()==0){
                break;
            }
        }
        taskList.addAll(tasks.values());
        return taskList;
    }

    /**
     * 修改任务发送数量
     * @param model
     */
    public boolean editTaskDetail(TaskConfigItem model){
        Log.i("automsg", generateKey(model) + "  ->  " + model.getTotal());
        if(tasks.get(generateKey(model)) == null ||
                model.getTotal()<0){
            return false;
        }
        UserDetailModel detailModel = commonService.getUserDetail();
        if (detailModel == null){
            return false;
        }
        int cost_max = detailModel.getCost_max();
        TaskConfigItem old = tasks.get(generateKey(model));
        //新设置数量导致总消费超限额
        if((old.getFee()*model.getTotal()-old.getCost()+getTotalSendCost())>cost_max){
            return false;
        }
        old.setTotal(model.getTotal());
        return true;
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

    public ICommonService getCommonService() {
        return commonService;
    }
}
