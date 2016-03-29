package com.round.aside.server.entity;

/**
 * 账号注册接口调用后的返回值，表明注册成功和失败以及失败对应的各种错误状态码
 * 
 * @author A Shuai
 * @date 2016-3-28
 *
 */
public final class RegisterResultEntity {

    //状态码，用于表征此次注册的结果，包括注册成功即登录成功，注册失败以及对应的各种错误状态
    private final int mStatusCode;
    //用户ID，只有注册成功才存在，注册成功即登录成功
    private final int mUserID;
    //令牌，只有注册成功才存在，注册成功即登录成功
    private final String mToken;

    //注册失败时使用的构造方法
    public RegisterResultEntity(int mStatusCode) {
        this.mStatusCode = mStatusCode;
        mUserID = -1;
        mToken = null;
    }

    //注册成功时使用的构造方法
    public RegisterResultEntity(int mStatusCode, int mUserID, String mToken) {
        this.mStatusCode = mStatusCode;
        this.mUserID = mUserID;
        this.mToken = mToken;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public int getUserID() {
        if(mUserID == -1){
            throw new IllegalStateException("This register is fail!");
        }
        return mUserID;
    }

    public String getToken() {
        if(mUserID == -1){
            throw new IllegalStateException("This register is fail!");
        }
        return mToken;
    }

}
