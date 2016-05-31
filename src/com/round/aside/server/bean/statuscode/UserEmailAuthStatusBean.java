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
        super(mBuilder.mPBuilder);
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
    public static class Builder {

        private String userEmail;
        private int userEmailStatusType;
        private final StatusCodeBean.Builder mPBuilder;

        public Builder() {
            userEmailStatusType = -1;
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

        public Builder setUserEmailStatusType(int userStatusType) {
            this.userEmailStatusType = userStatusType;
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

        public UserEmailAuthStatusBean build() {
            if (userEmailStatusType == -1) {
                throw new IllegalStateException(
                        "User Status type must be assign!");
            }
            return new UserEmailAuthStatusBean(this);
        }

    }

}
