package com.round.aside.server.bean.statuscode;

import com.round.aside.server.util.StringUtil;

/**
 * 包含UserID和Token的结果数据bean。<br>
 * 可用于注册成功和登陆成功使用。
 * 
 * @author A Shuai
 * @date 2016-5-27
 * 
 */
public class UserIDTokenSCBean extends StatusCodeBean {

    // 用户ID，只有注册成功才存在，注册成功即登录成功
    private final int userID;
    // 令牌，只有注册成功才存在，注册成功即登录成功
    private final String token;

    public UserIDTokenSCBean(Builder mBuilder) {
        super(mBuilder);
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
    public static class Builder extends StatusCodeBean.Builder {

        private int userID;
        private String token;

        public Builder() {
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

        public UserIDTokenSCBean build() {
            check();
            return new UserIDTokenSCBean(this);
        }

    }

}
