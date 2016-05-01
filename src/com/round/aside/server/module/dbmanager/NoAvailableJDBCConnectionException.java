package com.round.aside.server.module.dbmanager;

/**
 * 没有获得有效的JDBC连接异常，表明连接池已练满。需挂起等待有其他链接释放。
 * 
 * @author A Shuai
 * @date 2016-4-30
 *
 */
public class NoAvailableJDBCConnectionException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 575214153993123875L;
    
    public NoAvailableJDBCConnectionException() {
        super();
    }

    public NoAvailableJDBCConnectionException(String message) {
        super(message);
    }

    public NoAvailableJDBCConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
