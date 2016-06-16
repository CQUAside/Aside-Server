package com.round.aside.server.devenvir;

/**
 * 开发环境中用于测试的超级参数
 * 
 * @author A Shuai
 * @date 2016-6-15
 * 
 */
public final class DevGlobalParameter {

    private DevGlobalParameter() {
    }

    /**
     * 测试环境的超级管理员账号相关信息
     */
    public static final String ADMIN_ACCOUNT = "admin";
    public static final String ADMIN_PWD = "admin";
    public static final int ADMIN_USERID = 10000;
    public static final String ADMIN_TOKEN = "YTU4NTA5YWFmYzQ2ZmQ3YjJhMTY1OWU3MGU0N2M3Yjc=";

    /**
     * 数据库连接超时时长
     */
    public static final long DB_CONNECT_TIMEOUT = 60 * 1000;

}
