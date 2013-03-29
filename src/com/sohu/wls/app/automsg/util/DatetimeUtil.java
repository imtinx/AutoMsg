package com.sohu.wls.app.automsg.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: zhijieliu
 * Date: 13-3-20
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
public class DatetimeUtil {

    private static final Calendar CALENDAR = new GregorianCalendar();
    private static final SimpleDateFormat DATE_FORMATOR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 当前年份
     * @return
     */
    public static int getCurrentYear(){
        CALENDAR.setTime(new Date());
        return CALENDAR.get(Calendar.YEAR);
    }

    /**
     * 当前月份
     * @return
     */
    public static int getCurrentMonth(){
        CALENDAR.setTime(new Date());
        return CALENDAR.get(Calendar.MONTH)+1;
    }

    /**
     * 按yyyy-MM-dd HH:mm:ss格式化日期
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        if (date == null){
            return "";
        }

        return DATE_FORMATOR.format(date);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss转化为日期
     * @param date
     * @return
     */
    public static Date parseDate(String date){
         try {
             return DATE_FORMATOR.parse(date);
         } catch (Exception e){
             return null;
         }
    }
}
