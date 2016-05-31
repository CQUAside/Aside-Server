package com.round.aside.server.bean.statuscode;

import com.round.aside.server.util.StringUtil;

/**
 * 包含Email邮箱信息的状态码数据bean
 * 
 * @author A Shuai
 * @date 2016-5-31
 * 
 */
public class EmailStatusCodeBean extends StatusCodeBean {

    private final String userEmail;

    public EmailStatusCodeBean(Builder mBuilder) {
        super(mBuilder.mPBuilder);
        userEmail = mBuilder.userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }


    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-31
     * 
     */
    public static class Builder {

        private String userEmail;
        private final StatusCodeBean.Builder mPBuilder;

        public Builder() {
            mPBuilder = new StatusCodeBean.Builder();
        }

        public Builder setUserEmail(String userEmail) {
            if (StringUtil.isEmpty(userEmail)) {
                throw new IllegalArgumentException(
                        "the email str shouldn't be empty!");
            }
            this.userEmail = userEmail;
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

        public EmailStatusCodeBean build() {
            return new EmailStatusCodeBean(this);
        }

    }

}
