package com.sohu.wls.app.automsg.util;

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

    public static int getCurrentYear(){
        CALENDAR.setTime(new Date());
        return CALENDAR.get(Calendar.YEAR);
    }

    public static int getCurrentMonth(){
        CALENDAR.setTime(new Date());
        return CALENDAR.get(Calendar.MONTH)+1;
    }


}
