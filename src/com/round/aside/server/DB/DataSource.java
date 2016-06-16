package com.round.aside.server.DB;

import java.sql.Connection;
import java.sql.SQLException;

import com.round.aside.server.constant.DBConfig;
import com.round.aside.server.constant.GlobalParameter;
import com.round.aside.server.devenvir.DevGlobalParameter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

    private static HikariDataSource ds;

    private static final String DB_FROMAT = "jdbc:mysql://%s:%s/%s?useUnicode=true&amp;characterEncoding=UTF-8";
    
    private static final DBConfig DBCONFIG_INSTANCE = DBConfig.getInstance();

    public static void init() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(String.format(DB_FROMAT, DBCONFIG_INSTANCE.getDBIP(), DBCONFIG_INSTANCE.getDBPort(), DBCONFIG_INSTANCE.getDBName()));
        config.setUsername(DBCONFIG_INSTANCE.getDBAccount());
        config.setPassword(DBCONFIG_INSTANCE.getDBPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        if (GlobalParameter.DEV) {
            config.setConnectionTimeout(DevGlobalParameter.DB_CONNECT_TIMEOUT);
        }
        ds = new HikariDataSource(config);
    }

    /**
     * 销毁连接池
     */
    public void shutdown() {
        ds.shutdown();
    }

    /**
     * 从连接池中获取链接
     * 
     * @return
     */
    public static Connection getConnection() {
        if (ds == null) {
            init();
        }
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            ds.resumePool();
            return null;
        }
    }
}
