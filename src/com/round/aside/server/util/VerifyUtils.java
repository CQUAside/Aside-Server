package com.round.aside.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证对象的合法性。如是否为合法的手机号码等
 * 
 * @author A Shuai
 * @date 2016-4-30
 * 
 */
public final class VerifyUtils {

    private VerifyUtils() {
    }

    /**
     * 判断指定手机号是否为合法手机号
     * 
     * @param mPhoneNumber
     *            待验证合法性的手机号
     * @return true为合法，false为非法
     */
    public static boolean isPhoneNumber(String mPhoneNumber) {
        if (StringUtil.isEmpty(mPhoneNumber)) {
            return false;
        }

        Pattern mPattern = Pattern.compile("^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$");
        Matcher mMatcher = mPattern.matcher(mPhoneNumber);
        return mMatcher.matches();
    }

    /**
     * 检查是否是合法的图片文件格式。目前只支持jpg，png和jpeg。
     * 
     * @param mFileExten
     *            待检查的文件扩展名
     * @return true为合法，false为非法
     */
    public static boolean isLegalPicFormat(String mFileExten) {
        if (StringUtil.isEmpty(mFileExten)) {
            return false;
        }

        Pattern mPattern = Pattern.compile("^(jpg|png|jpeg)$");
        Matcher mMatcher = mPattern.matcher(mFileExten);
        return mMatcher.matches();
    }

}
