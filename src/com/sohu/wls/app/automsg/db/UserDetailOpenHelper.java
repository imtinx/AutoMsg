package com.sohu.wls.app.automsg.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sohu.wls.app.automsg.common.UserDetailModel;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-19
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class UserDetailOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String USERDETAIL_TABLE_NAME = "t_user_detail";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_VALUE = "VALUE";
    private static final String KEY_PHONENUMBER = "phone_number";
    private static final String KEY_COST_MAX = "cost_max";
    private static final String SQL_TABLE_CREATE =  "CREATE TABLE " + USERDETAIL_TABLE_NAME + " (" +
            COLUMN_NAME + " TEXT, " +
            COLUMN_VALUE + " TEXT);";


    public UserDetailOpenHelper(Context context) {
        super(context, USERDETAIL_TABLE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 初始化数据库
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //To change body of implemented methods use File | Settings | File Templates.
        sqLiteDatabase.execSQL(SQL_TABLE_CREATE);
        sqLiteDatabase.execSQL("INSERT INTO "+USERDETAIL_TABLE_NAME+"("+COLUMN_NAME+","+COLUMN_VALUE+")" +
                " VALUES('"+KEY_PHONENUMBER+"','');");
        sqLiteDatabase.execSQL("INSERT INTO "+USERDETAIL_TABLE_NAME+"("+COLUMN_NAME+","+COLUMN_VALUE+")" +
                " VALUES('"+KEY_COST_MAX+"',0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ USERDETAIL_TABLE_NAME);
//        sqLiteDatabase.execSQL(SQL_TABLE_CREATE);
    }

    /**
     * 查询存储的用户详情
     * @return
     * @throws Exception
     */
    public UserDetailModel queryUserDetailInfo() throws Exception{
        UserDetailModel detail = new UserDetailModel();
        SQLiteDatabase db = getDB();

        try {
            Cursor cursor = db.rawQuery("SELECT "+COLUMN_NAME+","+COLUMN_VALUE+" FROM "+USERDETAIL_TABLE_NAME,null);

            while (cursor.moveToNext()){
                 if (cursor.getString(0).equals(KEY_PHONENUMBER)){
                     detail.setPhone_number(cursor.getString(1));
                 }else if (cursor.getString(0).equals(KEY_COST_MAX)){
                     detail.setCost_max(cursor.getInt(1));
                 }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            db.close();
        }

        return detail;
    }

    /**
     * 更新用户信息
     * @param detail
     * @throws Exception
     */
    public void updateUserDetail(UserDetailModel detail) throws Exception{
        SQLiteDatabase db = getDB();
        try {
            db.execSQL("UPDATE " + USERDETAIL_TABLE_NAME + " SET " + COLUMN_VALUE + "='" + detail.getPhone_number() + "' WHERE " + COLUMN_NAME + "='" + KEY_PHONENUMBER + "'");
            db.execSQL("UPDATE " + USERDETAIL_TABLE_NAME + " SET " + COLUMN_VALUE + "=" + detail.getCost_max() + " WHERE " + COLUMN_NAME + "='" + KEY_COST_MAX + "'");
        } catch (SQLException e) {
            throw e;
        } finally {
            db.close();
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
