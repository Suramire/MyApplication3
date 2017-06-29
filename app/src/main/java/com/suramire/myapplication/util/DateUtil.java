package com.suramire.myapplication.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Suramire on 2017/6/29.
 */

public class DateUtil {
    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
