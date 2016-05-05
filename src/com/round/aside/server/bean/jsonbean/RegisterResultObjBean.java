package com.round.aside.server.bean.jsonbean;

/**
 * 注册结果Objbean
 * 
 * @author A Shuai
 * @date 2016-5-5
 * 
 */
public final class RegisterResultObjBean {

    private final int userID;
    private final String token;

    public RegisterResultObjBean(int userID, String token) {
        this.userID = userID;
        this.token = token;
    }

    public int getUserID() {
        return userID;
    }

    public String getToken() {
        return token;
    }

}
