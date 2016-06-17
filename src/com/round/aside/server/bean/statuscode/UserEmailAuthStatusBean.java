package com.round.aside.server.bean.statuscode;

import com.round.aside.server.enumeration.UserEmailStatusEnum;
import com.round.aside.server.util.StringUtil;

/**
 * 包含用户注册状态的状态码数据bean
 * 
 * @author A Shuai
 * @date 2016-5-29
 * 
 */
public class UserEmailAuthStatusBean extends StatusCodeBean {

    private final String userEmail;
    private final UserEmailStatusEnum userEmailStatus;

    public UserEmailAuthStatusBean(Builder mBuilder) {
        super(mBuilder);
        this.userEmail = mBuilder.userEmail;
        userEmailStatus = UserEmailStatusEnum
                .valueOf(mBuilder.userEmailStatusType);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public UserEmailStatusEnum getUserEmailStatus() {
        return userEmailStatus;
    }


    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-29
     * 
     */
    public static class Builder extends StatusCodeBean.Builder {

        private String userEmail;
        private int userEmailStatusType;

        public Builder() {
            userEmailStatusType = -1;
        }

        public Builder setUserEmail(String userEmail) {
            if (StringUtil.isEmpty(userEmail)) {
                throw new IllegalArgumentException(
                        "the email str shouldn't be empty!");
            }
            this.userEmail = userEmail;
            return this;
        }

        public Builder setUserEmailStatusType(int userStatusType) {
            this.userEmailStatusType = userStatusType;
            return this;
        }

        @Override
        protected void check() {
            super.check();
            if (userEmailStatusType == -1) {
                throw new IllegalStateException(
                        "User Status type must be assign!");
            }
        }

        @Override
        public UserEmailAuthStatusBean build() {
            check();
            return new UserEmailAuthStatusBean(this);
        }

    }

}
