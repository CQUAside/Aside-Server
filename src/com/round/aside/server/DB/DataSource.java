package com.round.aside.server.DB;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static com.round.aside.server.constant.DBConfig.*;

public class DataSource {

    private static HikariDataSource ds;

    private static final String DB_FROMAT = "jdbc:mysql://%s:%s/%s?useUnicode=true&amp;characterEncoding=UTF-8";

    public static void init() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(String.format(DB_FROMAT, DB_SERVER_IP, DB_SERVER_PORT, DB_SERVER_NAME));
        config.setUsername(DB_SERVER_ACCOUNT);
        config.setPassword(DB_SERVER_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
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
