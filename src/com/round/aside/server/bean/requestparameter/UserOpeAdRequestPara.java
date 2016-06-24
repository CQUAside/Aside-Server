package com.round.aside.server.bean.requestparameter;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.enumeration.AdStatusOpeEnum;
import com.round.aside.server.util.StringUtil;

/**
 * 用户操作广告状态Servlet对应的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public class UserOpeAdRequestPara extends UserIDTokenRequestPara {

    private final int adID;
    private final AdStatusOpeEnum adStatusOpe;

    public UserOpeAdRequestPara(Builder mTBuilder) {
        super(mTBuilder.mUserIDTokenRPBuilder);

        adID = mTBuilder.mAdID;
        adStatusOpe = mTBuilder.mAdStatusOpe;
    }

    public int getAdID() {
        return adID;
    }

    public AdStatusOpeEnum getAdStatusOpe() {
        return adStatusOpe;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            AbsRequestPara.AbsBuilder<UserOpeAdRequestPara> {

        private final UserIDTokenRequestPara.Builder mUserIDTokenRPBuilder;

        private int mAdID;
        private AdStatusOpeEnum mAdStatusOpe;

        public Builder() {
            super();

            mUserIDTokenRPBuilder = new UserIDTokenRequestPara.Builder();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);

            mUserIDTokenRPBuilder.onFillFieldKey(mParaSet);

            mParaSet.addKey("adID", true).addKey("adOpe", true);
        }

        @Override
        protected String onFillField(RequestParameterSet mParaSet) {
            String error = super.onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            error = mUserIDTokenRPBuilder.onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            try {
                mAdID = mParaSet.getValueAsInt("adID");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "Ad ID参数非法";
            }

            try {
                int mAdStatusOpeIndex = mParaSet.getValueAsInt("adState");
                mAdStatusOpe = AdStatusOpeEnum.valueOf(mAdStatusOpeIndex);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad 状态码参数非法";
            }

            return null;
        }

        @Override
        protected void setInitialized() {
            super.setInitialized();

            mUserIDTokenRPBuilder.setInitialized();
        }

        @Override
        protected String checkAfterFillField() {
            String error = super.checkAfterFillField();
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            error = mUserIDTokenRPBuilder.checkAfterFillField();
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            return null;
        }

        @Override
        protected void checkBeforeBuild() throws IllegalStateException {
            super.checkBeforeBuild();

            mUserIDTokenRPBuilder.checkBeforeBuild();
        }

        @Override
        protected UserOpeAdRequestPara buildInstance() {
            return new UserOpeAdRequestPara(this);
        }

    }

}
