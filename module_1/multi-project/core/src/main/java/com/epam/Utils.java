package com.epam;

public class Utils {
    public static boolean isAllPositiveNumbers(String...str){
        for (String s : str) {
            if (!StringUtils.isPositiveNumber(s)) {
                return false;
            }
        }
        return true;
    }
}
