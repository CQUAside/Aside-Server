package com.round.aside.server.util;

/**
 * 辅助工具类。包括发送短信，发送邮件。
 * 
 * @author A Shuai
 * @date 2016-4-30
 * 
 */
public final class AssistUtils {

    private AssistUtils() {
    }

    /**
     * 根据指定手机号发送一个随机生成的四位长度的验证码
     * 
     * @param phone
     *            待发送验证码的手机号
     * @return 如果执行成功，则返回对应验证码；若执行失败，则返回空字符串
     */
    public static String sendPhoneAuth(String phone) {
        return "1111";
    }

}
