package com.round.aside.server.DB;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

	private static HikariDataSource ds;

	public static void init() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("com.mysql.jdbc.Driver");
		config.setJdbcUrl("jdbc:mysql://rdsw5w6n4s4x11ln0re4.mysql.rds.aliyuncs.com:3306/rlcd502nx3sa25x1?useUnicode=true&amp;characterEncoding=UTF-8");
		config.setUsername("rlcd502nx3sa25x1");
		config.setPassword("7788120");
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
