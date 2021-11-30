package com.minkiapps.hos.test.util;

import ohos.utils.zson.ZSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date time util
 *
 * @since 2021-08-20
 */
public class DateUtils {
    private static final int TIME_LENGTH = 2;

    /**
     * current time
     *
     * @param format format
     * @return corresponding format string
     */
    public static String getCurrentDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        String formatDate = dateFormat.format(date);
        return formatDate;
    }

    /**
     * get the data that the page needs
     *
     * @return corresponding data
     */
    public static ZSONObject getZsonObject() {
        ZSONObject result = new ZSONObject();
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        result.put("hour", setTextValue(hour));
        int min = now.get(Calendar.MINUTE);
        result.put("min", setTextValue(min));
        int sec = now.get(Calendar.SECOND);
        result.put("sec", setTextValue(sec));
        return result;
    }

    /**
     * set text value
     *
     * @param now current time
     * @return corresponding format string
     */
    public static String setTextValue(int now) {
        String text = String.valueOf(now);
        if (text.length() < TIME_LENGTH) {
            text = "0" + text;
        } else {
            text = text + "";
        }
        return text;
    }
}
