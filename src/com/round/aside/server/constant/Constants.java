package com.round.aside.server.constant;

/**
 * 常量类
 * 
 * @author A Shuai
 * @date 2016-4-21
 *
 */
public final class Constants {

    private Constants(){}

    /**
     * 邮件发送方的地址
     */
    public static final String SEND_EMAIL_ADDRESS = "";
    /**
     * 邮件发送方密码
     */
    public static final String SEND_EMAIL_PASSWORD = "";

    /**
     * APP的真实根目录
     */
    public static final String ROOT_DIR = "..\\webapps\\Aside-Server\\";

    /**
     * 原图真实目录
     */
    public static final String ORI_IMG_DIR = ROOT_DIR + "OriginalImg\\";

    /**
     * 缩略图真实目录
     */
    public static final String THUMB_IMG_DIR = ROOT_DIR + "ThumbImg\\";

    /**
     * Logo图真实目录
     */
    public static final String LOGO_IMG_DIR = ROOT_DIR + "LogoImg\\";

    /**
     * 邮件发送服务器
     */
    public static final String EMAIL_SMTP_SERVER = "smtp.sina.cn";


}
