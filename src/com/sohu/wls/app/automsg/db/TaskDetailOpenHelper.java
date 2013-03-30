package com.sohu.wls.app.automsg.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.sohu.wls.app.automsg.common.SMSTaskModel;
import com.sohu.wls.app.automsg.taskconfig.TaskConfigMainActivity;
import com.sohu.wls.app.automsg.util.DatetimeUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-20
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class TaskDetailOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TASKDETAIL_TABLE_NAME = "t_task_detail";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DESTNUMBER = "destnumber";
    private static final String COLUMN_SENDED = "sended";
    private static final String COLUMN_RECEIVED = "received";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_STARTTIME = "starttime";
    private static final String COLUMN_RECEIVETIME = "receivetime";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE " + TASKDETAIL_TABLE_NAME + " (" +
            COLUMN_ID + " TEXT, " +
            COLUMN_CONTENT + " TEXT," +
            COLUMN_DESTNUMBER + " TEXT," +
            COLUMN_SENDED + " TEXT," +
            COLUMN_RECEIVED + " TEXT," +
            COLUMN_YEAR + " TEXT," +
            COLUMN_MONTH + " TEXT," +
            COLUMN_STARTTIME + " TEXT," +
            COLUMN_RECEIVETIME + " TEXT );";



    public TaskDetailOpenHelper(Context context) {
        super(context, TASKDETAIL_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
        sqLiteDatabase.execSQL(SQL_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 新增任务
     * @throws Exception
     */
    public void addTask(SMSTaskModel task) throws Exception{


        if (task == null){
            throw new NullPointerException("param task should not be null");
        }

        detectNecessaryField(task);

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(1) FROM " + TASKDETAIL_TABLE_NAME + " WHERE " +
                    COLUMN_ID + "='" + task.getTask_id() + "'", null);

            if (cursor.moveToNext() && cursor.getInt(0) > 0){

                throw new RuntimeException("task id is exist");
            }

            String year = DatetimeUtil.getCurrentYear()+"";
            String month = DatetimeUtil.getCurrentMonth()+"";


            db.execSQL("INSERT INTO " + TASKDETAIL_TABLE_NAME + "(" + COLUMN_ID + "" +
                    ","+COLUMN_CONTENT+
                    ","+COLUMN_DESTNUMBER+
                    ","+COLUMN_YEAR+
                    ","+COLUMN_MONTH+
                    ","+COLUMN_SENDED+
                    ","+COLUMN_RECEIVED+") VALUES(" +
                    "'" +task.getTask_id()+
                    "','"+task.getSms_content()+
                    "','"+task.getSms_destnumber()+
                    "','"+year+
                    "','"+month+
                    "','"+SMSTaskModel.VALUE_NOT_SENDED+
                    "','"+SMSTaskModel.VALUE_NOT_RECEIVED+"')");
        } catch (RuntimeException e) {
            throw e;
        } finally {
            if (cursor != null){
                cursor.close();
            }
            db.close();
        }
    }

    /**
     * 更新任务信息
     *
     * @param task
     * @throws Exception
     */
    public void updateTask(SMSTaskModel task) throws Exception{
        if (task == null){
            throw new NullPointerException("param task should not be null");
        }
        if (task.getTask_id() == null || task.getTask_id().equals("")){
            throw new NullPointerException("task_id should not be null");
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE " + TASKDETAIL_TABLE_NAME + " SET ");

        if (task.isSms_sended()){
            buffer.append(COLUMN_SENDED + "='"+SMSTaskModel.VALUE_SENDED+"',");
        }else{
            buffer.append(COLUMN_SENDED + "='"+SMSTaskModel.VALUE_NOT_SENDED+"',");
        }

        if (task.isSms_received()){
            buffer.append(COLUMN_RECEIVED + "='"+SMSTaskModel.VALUE_RECEIVED+"',");
        }else{
            buffer.append(COLUMN_RECEIVED + "='"+SMSTaskModel.VALUE_NOT_RECEIVED+"',");
        }

        if (task.getStarttime() != null){
             buffer.append(COLUMN_STARTTIME + "='" + DatetimeUtil.formatDate(task.getStarttime()) + "',");
        }

        if (task.getRecivetime() != null){
            buffer.append(COLUMN_RECEIVETIME + "='" + DatetimeUtil.formatDate(task.getRecivetime()) + "',");
        }

        if(buffer.toString().endsWith(",")){
            buffer.delete(buffer.length()-1,buffer.length());
        }

        buffer.append(" WHERE " + COLUMN_ID + "='" + task.getTask_id() + "'");

        SQLiteDatabase db = getDB();
        Log.v(TaskConfigMainActivity.TAG, buffer.toString());
        try {
            db.execSQL(buffer.toString());
        } catch (SQLException e) {
            throw e;
        } finally {
            db.close();
        }

    }

    /**
     * 查询某月的任务列表
     * @param year  指定年份
     * @param month 指定月份
     * @return
     */
    public List<SMSTaskModel> queryTasks(int year, int month){
        List<SMSTaskModel> tasks = new ArrayList<SMSTaskModel>();

        if (year == 0 || month == 0){
            return tasks;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append("SELECT ") ;
        buffer.append(COLUMN_ID + ",");
        buffer.append(COLUMN_CONTENT + ",");
        buffer.append(COLUMN_DESTNUMBER + ",");
        buffer.append(COLUMN_SENDED + ",");
        buffer.append(COLUMN_RECEIVED + ",");
        buffer.append(COLUMN_YEAR + ",");
        buffer.append(COLUMN_MONTH + ",");
        buffer.append(COLUMN_STARTTIME + ",");
        buffer.append(COLUMN_RECEIVETIME + " ");
        buffer.append("FROM " + TASKDETAIL_TABLE_NAME + " ");
        buffer.append("WHERE ");

        buffer.append(COLUMN_YEAR + "=?");
        buffer.append(" AND " + COLUMN_MONTH + "=?");

        buffer.append(" ORDER BY " + COLUMN_STARTTIME + " DESC");

        SQLiteDatabase db = getDB();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(buffer.toString(),new String[]{year+"",month+""});
            while (cursor.moveToNext()){
                SMSTaskModel task = new SMSTaskModel(cursor.getString(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(5),cursor.getInt(6));
                if (SMSTaskModel.VALUE_SENDED.equals(cursor.getString(3))){
                    task.setSms_sended(true);
                }else {
                    task.setSms_sended(false);
                }
                if (SMSTaskModel.VALUE_RECEIVED.equals(cursor.getString(4))){
                    task.setSms_received(true);
                }else {
                    task.setSms_received(false);
                }
                if (cursor.getString(7)!=null && cursor.getString(7).length()>0){

                    task.setStarttime(DatetimeUtil.parseDate(cursor.getString(7)));

                }
                if (cursor.getString(8)!=null && cursor.getString(8).length()>0){

                    task.setRecivetime(DatetimeUtil.parseDate(cursor.getString(8)));

                }
                tasks.add(task);
            }
        } catch (Exception e) {
            return tasks;
        } finally {
            if (cursor != null){
                cursor.close();
            }
            db.close();
        }

        return tasks;
    }

    /**
     * 根据任务ID，查找发送任务
     * @param id
     * @return
     * @throws Exception
     */
    public SMSTaskModel queryTaskByID(String id) throws Exception{

        if (id == null || id.equals("")){
            return null;
        }
        SQLiteDatabase db = getDB();

        StringBuffer buffer = new StringBuffer();

        buffer.append("SELECT ") ;
        buffer.append(COLUMN_ID + ",");
        buffer.append(COLUMN_CONTENT + ",");
        buffer.append(COLUMN_DESTNUMBER + ",");
        buffer.append(COLUMN_SENDED + ",");
        buffer.append(COLUMN_RECEIVED + ",");
        buffer.append(COLUMN_YEAR + ",");
        buffer.append(COLUMN_MONTH + ",");
        buffer.append(COLUMN_STARTTIME + ",");
        buffer.append(COLUMN_RECEIVETIME + " ");
        buffer.append("FROM " + TASKDETAIL_TABLE_NAME + " ");
        buffer.append("WHERE ");

        buffer.append(COLUMN_ID + "=?");
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(buffer.toString(),new String[]{id});
            if (cursor.moveToNext()){
                SMSTaskModel task = new SMSTaskModel(cursor.getString(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(5),cursor.getInt(6));
                if (SMSTaskModel.VALUE_SENDED.equals(cursor.getString(3))){
                    task.setSms_sended(true);
                }else {
                    task.setSms_sended(false);
                }
                if (SMSTaskModel.VALUE_RECEIVED.equals(cursor.getString(4))){
                    task.setSms_received(true);
                }else {
                    task.setSms_received(false);
                }
                if (cursor.getString(7)!=null && cursor.getString(7).length()>0){

                    task.setStarttime(DatetimeUtil.parseDate(cursor.getString(7)));

                }
                if (cursor.getString(8)!=null && cursor.getString(8).length()>0){

                    task.setRecivetime(DatetimeUtil.parseDate(cursor.getString(8)));

                }

                return task;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null){
                cursor.close();
            }
            db.close();
        }
    }

    /**
     * 根据收到短信的号码，查找最早的未收到回复的发送任务
     * @param spcode
     * @return
     */
    public SMSTaskModel queryLastSentTask(String spcode) throws Exception{
        if (spcode == null || spcode.equals("")){
            return null;
        }
        SQLiteDatabase db = getDB();

        StringBuffer buffer = new StringBuffer();

        buffer.append("SELECT ") ;
        buffer.append(COLUMN_ID + ",");
        buffer.append(COLUMN_CONTENT + ",");
        buffer.append(COLUMN_DESTNUMBER + ",");
        buffer.append(COLUMN_SENDED + ",");
        buffer.append(COLUMN_RECEIVED + ",");
        buffer.append(COLUMN_YEAR + ",");
        buffer.append(COLUMN_MONTH + ",");
        buffer.append(COLUMN_STARTTIME + ",");
        buffer.append(COLUMN_RECEIVETIME + " ");
        buffer.append("FROM " + TASKDETAIL_TABLE_NAME + " ");
        buffer.append("WHERE ");

        buffer.append(COLUMN_SENDED + "=?");
        buffer.append(" AND " + COLUMN_RECEIVED + "=?");
        buffer.append(" AND " + COLUMN_YEAR + "=?");
        buffer.append(" AND " + COLUMN_MONTH + "=?");
        buffer.append(" AND " + COLUMN_DESTNUMBER + "=?");

        buffer.append(" ORDER BY " + COLUMN_STARTTIME );

        Log.v(TaskConfigMainActivity.TAG,buffer.toString());

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(buffer.toString(),new String[]{SMSTaskModel.VALUE_SENDED,SMSTaskModel.VALUE_NOT_RECEIVED,
                    DatetimeUtil.getCurrentYear()+"",DatetimeUtil.getCurrentMonth()+"",spcode});
            if (cursor.moveToNext()){
                SMSTaskModel task = new SMSTaskModel(cursor.getString(0),cursor.getString(1),
                        cursor.getString(2),cursor.getInt(5),cursor.getInt(6));
                if (SMSTaskModel.VALUE_SENDED.equals(cursor.getString(3))){
                    task.setSms_sended(true);
                }else {
                    task.setSms_sended(false);
                }
                if (SMSTaskModel.VALUE_RECEIVED.equals(cursor.getString(4))){
                    task.setSms_received(true);
                }else {
                    task.setSms_received(false);
                }
                if (cursor.getString(7)!=null && cursor.getString(7).length()>0){

                    task.setStarttime(DatetimeUtil.parseDate(cursor.getString(7)));

                }
                if (cursor.getString(8)!=null && cursor.getString(8).length()>0){

                    task.setRecivetime(DatetimeUtil.parseDate(cursor.getString(8)));

                }

                return task;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null){
                cursor.close();
            }
            db.close();
        }
    }

    //检查任务字段值
    private void detectNecessaryField(SMSTaskModel task) throws IllegalArgumentException{
        if (task.getTask_id() == null || task.getTask_id().equals("")){
            throw new IllegalArgumentException("task id should not be null or empty");
        }
        if (task.getSms_content() == null || task.getSms_content().equals("")){
            throw new IllegalArgumentException("task sms_content should not be null or empty");
        }
        if (task.getSms_destnumber() == null || task.getSms_destnumber().equals("")){
            throw new IllegalArgumentException("task sms_destnumber should not be null or empty");
        }
    }

    public SQLiteDatabase getDB() throws NullPointerException{
        SQLiteDatabase db = getWritableDatabase();

        if(db == null){
            throw new NullPointerException("db can't be initted");
        }

        return db;
    }
}
