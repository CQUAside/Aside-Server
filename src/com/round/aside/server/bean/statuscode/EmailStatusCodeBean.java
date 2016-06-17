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
        super(mBuilder);
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
    public static class Builder extends StatusCodeBean.Builder {

        private String userEmail;

        public Builder() {
        }

        public Builder setUserEmail(String userEmail) {
            if (StringUtil.isEmpty(userEmail)) {
                throw new IllegalArgumentException(
                        "the email str shouldn't be empty!");
            }
            this.userEmail = userEmail;
            return this;
        }

        public EmailStatusCodeBean build() {
            check();
            return new EmailStatusCodeBean(this);
        }

    }

}
