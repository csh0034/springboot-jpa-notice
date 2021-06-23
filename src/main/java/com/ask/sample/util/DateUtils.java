package com.ask.sample.util;

import com.ask.sample.constant.Constant;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        return sdf.format(date);
    }

    public static String formatDefault(Date date) {
        return format(date, Constant.DATE_FORMAT);
    }

    public static String formatNow(String pattern) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
        return sdf.format(date);
    }
}