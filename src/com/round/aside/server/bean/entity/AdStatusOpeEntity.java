package com.round.aside.server.bean.entity;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.enumeration.AdStatusOpeEnum;

/**
 * 广告状态操作实体类
 * 
 * @author A Shuai
 * @date 2016-6-21
 * 
 */
public final class AdStatusOpeEntity {

    private final int adID;
    private final AdStatusOpeEnum adStatusOpe;

    public AdStatusOpeEntity(Builder mBuilder) {
        adID = mBuilder.mAdID;
        adStatusOpe = mBuilder.mAdStatusOpe;
    }

    public int getAdID() {
        return adID;
    }

    public AdStatusOpeEnum getAdStatusOpe() {
        return adStatusOpe;
    }

    /**
     * 建造者类
     * 
     * @author A Shuai
     * @date 2016-6-21
     * 
     */
    public static class Builder {

        private boolean init;

        private int mAdID;
        private AdStatusOpeEnum mAdStatusOpe;

        public Builder() {
            init = false;
        }

        /**
         * 填充字段
         * 
         * @param mParaSet
         *            参数集
         * @return 成功返回null，失败返回错误信息
         */
        public String fillField(RequestParameterSet mParaSet) {
            String mAdIDStr = mParaSet.getValue("adID");
            try {
                mAdID = Integer.parseInt(mAdIDStr);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad ID参数非法";
            }

            String mAdStatusOpeIndexStr = mParaSet.getValue("adState");
            try {
                int mAdStatusOpeIndex = Integer.parseInt(mAdStatusOpeIndexStr);
                mAdStatusOpe = AdStatusOpeEnum.valueOf(mAdStatusOpeIndex);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad 状态码参数非法";
            }

            init = true;
            return null;
        }

        public AdStatusOpeEntity build() {
            if (!init) {
                throw new IllegalStateException(
                        "the builder of operate ad entity must be inited!");
            }
            return new AdStatusOpeEntity(this);
        }

    }

}
