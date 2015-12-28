package com.round.aside.server.util;

/**
 * 字符串操作相关的工具类
 * 
 * @author A Shuai
 * 
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 检查提供的字符串是否为空
     * 
     * @param str
     *            待检查的字符串
     * @return true为空
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

}
