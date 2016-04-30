package com.round.aside.server.util;

/**
 * 字符串操作相关的工具类
 * 
 * @author A Shuai
 * 
 */
public final class StringUtil {

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
    
    /**
     * 字符串连接
     * 
     * @param mStrs
     *            不定长String对象
     * @return 拼接后的字符串
     */
    public static String concat(String... mStrs) {
        if (mStrs == null) {
            return null;
        }

        if (mStrs.length == 0) {
            return "";
        }
        if (mStrs.length == 1) {
            return mStrs[0];
        }

        StringBuilder mStrBuilder = new StringBuilder();
        for (int i = 0; i < mStrs.length; i++) {
            mStrBuilder.append(mStrs[i]);
        }

        return mStrBuilder.toString();
    }

}
