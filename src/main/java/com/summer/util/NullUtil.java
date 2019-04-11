package com.summer.util;

public class NullUtil {
    public NullUtil() {
    }

    public static Object isEmpty(Object str, Object o) {
        return str == null?o:str;
    }

    public static boolean isStrEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNull(Object... o) {
        for(int i = 0; i < o.length; ++i) {
            if(o[i] == null) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNull(Object o) {
        return o == null;
    }
}