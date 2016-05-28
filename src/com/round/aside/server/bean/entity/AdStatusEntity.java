package com.round.aside.server.bean.entity;

import com.round.aside.server.bean.statuscode.AdStatusCodeBean;
import com.round.aside.server.enumeration.AdStatusEnum;

/**
 * 广告状态数据bean，分别为广告状态，点击数，收藏数
 * 
 * @author A Shuai
 * @date 2016-5-28
 * 
 */
public final class AdStatusEntity {

    private final AdStatusEnum adStatus;
    private final int clickCount;
    private final int collectCount;

    public AdStatusEntity(AdStatusEnum adStatus, int clickCount,
            int collectCount) {
        this.adStatus = adStatus;
        this.clickCount = clickCount;
        this.collectCount = collectCount;
    }

    public AdStatusEntity(Builder mBuilder) {
        adStatus = AdStatusEnum.valueOf(mBuilder.adStatusType);
        clickCount = mBuilder.clickCount;
        collectCount = mBuilder.collectCount;
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

    public static class Builder {

        private int adStatusType = -1;
        private int clickCount;
        private int collectCount;

        public Builder() {
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

        public Builder setAdStatusCodeBean(AdStatusCodeBean mAdStatusBean) {
            adStatusType = mAdStatusBean.getAdStatus().getType();
            clickCount = mAdStatusBean.getClickCount();
            collectCount = mAdStatusBean.getCollectCount();
            return this;
        }

        public AdStatusEntity build() {
            if (adStatusType == -1) {
                throw new IllegalStateException(
                        "the ad status type must be assigned!");
            }
            if (clickCount < 0) {
                throw new IllegalStateException(
                        "the click count shouldn't be negative");
            }
            if (collectCount < 0) {
                throw new IllegalStateException(
                        "the collect count shouldn't be negative");
            }
            return new AdStatusEntity(this);
        }

    }

}
