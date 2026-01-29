package com.mecaroid.interdate.ReturningValues;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import java.util.Date;

public class TimeStamp {

    public static String timeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(new Date());

    }
}
