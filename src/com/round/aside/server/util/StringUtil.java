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
     * 检查参数指定的字符串序列结合是否只是一个为空
     * 
     * @param mStrs
     *            待检查的字符串序列集合，不定长参数
     * @return true为至少一个为空，false为全部为空
     */
    public static boolean isEmptyInSet(CharSequence... mStrs) {
        if (mStrs == null || mStrs.length == 0) {
            throw new IllegalStateException(
                    "the parameter shouldn't be null or empty totally");
        }

        for (CharSequence str : mStrs) {
            if (isEmpty(str)) {
                return true;
            }
        }

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
