package com.epam;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    public static boolean isPositiveNumber(String str){
        return NumberUtils.createInteger(str) > 0;
    }
}
