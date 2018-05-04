package com.blackspider.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by USER on 3/12/2018.
 */

public class Converter {
    public static String toDateStr(long milliseconds)
    {
        String format = "dd/MM/yy hh:mm";
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }

    public static long byteToKb(long bytes){
        return bytes/1024;
    }
}
