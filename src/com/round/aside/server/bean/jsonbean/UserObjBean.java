package com.round.aside.server.bean.jsonbean;

/**
 * 用户信息Obj bean
 * 
 * @author A Shuai
 * @date 2016-5-5
 * 
 */
public final class UserObjBean {

    private final int userID;
    private final String token;

    public UserObjBean(int userID, String token) {
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
