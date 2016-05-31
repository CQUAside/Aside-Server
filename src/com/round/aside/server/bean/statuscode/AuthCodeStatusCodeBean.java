package com.round.aside.server.bean.statuscode;

/**
 * 邮件认证码的状态码数据bean
 * 
 * @author A Shuai
 * @date 2016-5-29
 * 
 */
public class AuthCodeStatusCodeBean extends StatusCodeBean {

    private final String authCode;

    public AuthCodeStatusCodeBean(Builder mBuilder) {
        super(mBuilder.mPBuilder);
        this.authCode = mBuilder.authCode;
    }

    public String getAuthCode() {
        return authCode;
    }


    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-29
     * 
     */
    public static class Builder {

        private String authCode;
        private final StatusCodeBean.Builder mPBuilder;

        public Builder() {
            mPBuilder = new StatusCodeBean.Builder();
        }

        public Builder setAuthCode(String authCode) {
            this.authCode = authCode;
            return this;
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

        public AuthCodeStatusCodeBean build() {
            return new AuthCodeStatusCodeBean(this);
        }

    }

}
