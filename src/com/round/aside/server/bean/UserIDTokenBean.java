package com.round.aside.server.bean;

/**
 * 包含用户ID和Token的
 * 
 * @author A Shuai
 * @date 2016-6-11
 * 
 */
public class UserIDTokenBean {

    private final int mUserID;
    private final String mToken;

    public UserIDTokenBean(Builder mBuilder) {
        mUserID = mBuilder.mUserID;
        mToken = mBuilder.mToken;
    }

    public int getUserID() {
        return mUserID;
    }

    public String getToken() {
        return mToken;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-11
     * 
     */
    public static class Builder {

        private int mUserID;
        private String mToken;

        public Builder() {
        }

        public Builder setUserID(int mUserID) {
            this.mUserID = mUserID;
            return this;
        }

        public Builder setToken(String mToken) {
            this.mToken = mToken;
            return this;
        }

        public UserIDTokenBean build() {
            return new UserIDTokenBean(this);
        }

    }

}
