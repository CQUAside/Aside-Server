package com.round.aside.server.constant;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * 数据库的配置信息类。单例模式，最为安全的实现方式之一。
 * 
 * @author A Shuai
 * @date 2016-4-25
 * 
 */
public final class DBConfig {

    private static final String DBCONFIG_FILE = "DBConfig.xml";

    public static DBConfig getInstance() {
        return DBConfigInner.INSTANCE;
    }

    private final String mDBIP;
    private final String mDBPort;
    private final String mDBName;
    private final String mDBAccount;
    private final String mDBPassword;

    private DBConfig() {

        Properties mProperties = new Properties();
        try {
            mProperties.loadFromXML(new FileInputStream(new File("..\\webapps\\Aside-Server\\config\\" + DBCONFIG_FILE)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(DBCONFIG_FILE + " file not found!");
        }

        mDBIP = mProperties.getProperty("DBServerIP");
        mDBPort = mProperties.getProperty("DBServerPort");
        mDBName = mProperties.getProperty("DBServerName");
        mDBAccount = mProperties.getProperty("DBServerAccount");
        mDBPassword = mProperties.getProperty("DBServerPassword");

    }

    /**
     * 获取远程服务器数据库IP地址
     * 
     * @return
     */
    public String getDBIP() {
        return mDBIP;
    }

    /**
     * 获取远程服务器数据库端口号
     * 
     * @return
     */
    public String getDBPort() {
        return mDBPort;
    }

    /**
     * 获取远程服务器数据库名
     * 
     * @return
     */
    public String getDBName() {
        return mDBName;
    }

    /**
     * 获取登陆远程服务器数据库的账号
     * 
     * @return
     */
    public String getDBAccount() {
        return mDBAccount;
    }

    /**
     * 获取登陆服务器数据库的密码
     * 
     * @return
     */
    public String getDBPassword() {
        return mDBPassword;
    }

    /**
     * 内部类实例化并保存唯一的外部类实例。
     * 
     * @author A Shuai
     * @date 2016-4-28
     * 
     */
    private static class DBConfigInner {

        private static final DBConfig INSTANCE = new DBConfig();

    }

}
