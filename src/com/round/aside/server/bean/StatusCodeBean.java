package com.round.aside.server.bean;

/**
 * 状态码的数据bean，包含了状态码以及描述信息
 * 
 * @author A Shuai
 * @date 2016-5-25
 *
 */
public final class StatusCodeBean {

    private final int statusCode;
    private final String msg;

    public StatusCodeBean(int statusCode, String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

}
