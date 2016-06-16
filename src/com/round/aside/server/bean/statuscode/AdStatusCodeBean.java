package com.round.aside.server.bean.statuscode;

import com.round.aside.server.enumeration.AdStatusEnum;

/**
 * 广告及状态码数据bean
 * 
 * @author A Shuai
 * @date 2016-5-28
 * 
 */
public class AdStatusCodeBean extends StatusCodeBean {

    private final AdStatusEnum adStatus;
    private final int clickCount;
    private final int collectCount;
    private final int userID;

    public AdStatusCodeBean(Builder mBuilder) {
        super(mBuilder.mPBuilder);
        adStatus = AdStatusEnum.valueOf(mBuilder.adStatusType);
        clickCount = mBuilder.clickCount;
        collectCount = mBuilder.collectCount;
        userID = mBuilder.userID;
    }

    public AdStatusEnum getAdStatus() {
        return adStatus;
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public int getUserID() {
        return userID;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-28
     * 
     */
    public static class Builder {

        private int adStatusType;
        private int clickCount;
        private int collectCount;
        private int userID;
        private final StatusCodeBean.Builder mPBuilder;

        public Builder() {
            mPBuilder = new StatusCodeBean.Builder();
        }

        public Builder setAdStatusType(int adStatusType) {
            this.adStatusType = adStatusType;
            return this;
        }

        public Builder setClickCount(int clickCount) {
            this.clickCount = clickCount;
            return this;
        }

        public Builder setCollectCount(int collectCount) {
            this.collectCount = collectCount;
            return this;
        }

        public Builder setUserID(int userID) {
            this.userID = userID;
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

        public AdStatusCodeBean build() {
            return new AdStatusCodeBean(this);
        }

    }

}