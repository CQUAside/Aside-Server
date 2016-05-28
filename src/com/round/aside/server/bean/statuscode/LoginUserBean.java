package com.round.aside.server.bean.statuscode;

import com.round.aside.server.util.StringUtil;

/**
 * 用户登录类接口调用的结果数据bean，包含了UserID和Token。可用于注册成功和登陆成功使用
 * 
 * @author A Shuai
 * @date 2016-5-27
 * 
 */
public class LoginUserBean extends StatusCodeBean {

    // 用户ID，只有注册成功才存在，注册成功即登录成功
    private final int userID;
    // 令牌，只有注册成功才存在，注册成功即登录成功
    private final String token;

    public LoginUserBean(Builder mBuilder) {
        super(mBuilder.mPBuilder);
        userID = mBuilder.userID;
        token = mBuilder.token;
    }

    public int getUserID() {
        return userID;
    }

    public String getToken() {
        return token;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-27
     * 
     */
    public static class Builder {

        private int userID;
        private String token;
        private final StatusCodeBean.Builder mPBuilder;

        public Builder() {
            mPBuilder = new StatusCodeBean.Builder();
        }

        public Builder setStatusCodeBean(StatusCodeBean statusCodeBean) {
            mPBuilder.setStatusCode(statusCodeBean.getStatusCode());
            mPBuilder.setMsg(statusCodeBean.getMsg());
            return this;
        }

        public Builder setStatusCode(int statusCode) {
            mPBuilder.setStatusCode(statusCode);
            return this;
        }

        public Builder setMsg(String msg) {
            mPBuilder.setMsg(msg);
            return this;
        }

        public Builder setUserID(int userID) {
            this.userID = userID;
            return this;
        }

        public Builder setToken(String token) {
            if (StringUtil.isEmpty(token)) {
                throw new IllegalArgumentException(
                        "the token shouldn't be empty!");
            }
            this.token = token;
            return this;
        }

        public LoginUserBean build() {
            return new LoginUserBean(this);
        }

    }

}
