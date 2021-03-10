package com.ask.sample.util;

import java.util.UUID;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String getNewId(String prefix) {
        return prefix + UUID.randomUUID().toString().replaceAll("-", "");
    }
}
