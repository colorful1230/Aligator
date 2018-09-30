package com.aligator.filecleaner.utils;

public class SafeUtils {

    public static <T> T checkNotNull(T t) {
        if (t == null) {
            throw new RuntimeException("The parameter must not be null");
        }
        return t;
    }
}
