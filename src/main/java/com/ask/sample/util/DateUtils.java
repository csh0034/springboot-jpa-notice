package com.ask.sample.util;

import com.ask.sample.constant.Constants;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

  public static String format(Date date, String pattern) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }

  public static String formatNow(String pattern) {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }

  public static DateTimeFormatter getDefaultTimeFormatter() {
    return DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
  }
}