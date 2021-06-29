package com.ask.sample.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

public final class StringUtils extends org.apache.commons.lang3.StringUtils {

  public static String getNewId(String prefix) {
    return prefix + UUID.randomUUID().toString().replaceAll("-", "");
  }

  public static String getDownloadFilename(String fileName) {
    String fname;
    try {
      fname = URLEncoder.encode(fileName, "utf-8");
    } catch (UnsupportedEncodingException e) {
      return fileName;
    }
    String[] regexs = new String[]{
        "\\%29", "\\%28", "\\%3D", "\\%2B", "\\%7B",
        "\\%7D", "\\%27", "\\%26", "\\%21", "\\%40",
        "\\%23", "%24", "\\%25", "\\%5E", "\\%26",
        "\\%2C", "\\%5B", "\\%5D", "\\+", "\\%2F"};
    String[] replchars = new String[]{
        ")", "(", "=", "+", "{",
        "}", "'", "&", "!", "@",
        "#", "\\$", "%", "^", "&",
        ",", "[", "]", " ", ","};

    for (int i = 0; i < replchars.length; i++) {
      fname = fname.replaceAll(regexs[i], replchars[i]);
    }
    return fname;
  }
}
