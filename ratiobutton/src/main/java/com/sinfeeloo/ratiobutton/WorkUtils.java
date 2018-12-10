package com.sinfeeloo.ratiobutton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: mhj
 * @date: 2018/12/7 11:42
 * @desc:
 */
public class WorkUtils {
    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
