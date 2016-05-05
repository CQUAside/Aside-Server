package com.round.aside.server.entity;

/**
 * 账号实体类
 * 
 * @author A Shuai
 * @date 2016-5-5
 * 
 */
public final class LoginUserEntity {

    private final int mStatuscode;
    private final int mUserID;
    private final String mToken;

    public LoginUserEntity(Builder mBuilder) {
        mStatuscode = mBuilder.mStatuscode;
        mUserID = mBuilder.mUserID;
        mToken = mBuilder.mToken;
    }

    public int getStatuscode() {
        return mStatuscode;
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
     * @date 2016-5-5
     * 
     */
    public static class Builder {

        private int mStatuscode;
        private int mUserID;
        private String mToken;

        public Builder() {
        }

        public Builder setStatuscode(int mStatuscode) {
            this.mStatuscode = mStatuscode;
            return this;
        }

        public int getStatuscode() {
            return mStatuscode;
        }

        public Builder setUserID(int mUserID) {
            this.mUserID = mUserID;
            return this;
        }

        public int getUserID() {
            return mUserID;
        }

        public Builder setToken(String mToken) {
            this.mToken = mToken;
            return this;
        }

        public String getToken() {
            return mToken;
        }

        public LoginUserEntity build() {
            return new LoginUserEntity(this);
        }

    }

}
