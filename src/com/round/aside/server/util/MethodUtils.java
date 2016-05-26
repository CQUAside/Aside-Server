package com.round.aside.server.util;

/**
 * 方法工具类
 * 
 * @author A Shuai
 * @date 2016-5-22
 * 
 */
public final class MethodUtils {

    private MethodUtils() {
    }

    /**
     * 关闭可关闭对象
     * 
     * @param mAutoCloseableArray
     *            可关闭对象，不定长参数
     */
    public static void closeAutoCloseable(AutoCloseable... mAutoCloseableArray) {
        if (mAutoCloseableArray == null || mAutoCloseableArray.length == 0) {
            return;
        }
        for (AutoCloseable mAutoCloseable : mAutoCloseableArray) {
            try {
                mAutoCloseable.close();
            } catch (Exception e1) {
            }
        }
    }

}
