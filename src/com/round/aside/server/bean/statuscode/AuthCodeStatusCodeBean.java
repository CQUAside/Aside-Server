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
        super(mBuilder);
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
    public static class Builder extends StatusCodeBean.Builder {

        private String authCode;

        public Builder() {
        }

        public Builder setAuthCode(String authCode) {
            this.authCode = authCode;
            return this;
        }

        public AuthCodeStatusCodeBean build() {
            check();
            return new AuthCodeStatusCodeBean(this);
        }

    }

}
