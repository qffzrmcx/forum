package com.qffz.utils;

public class StringUtil {
    private StringUtil() {}

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String trim(String value) {
        return value == null ? null : value.trim();
    }
}
