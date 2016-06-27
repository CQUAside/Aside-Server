package com.round.aside.server.bean.requestparameter;

import java.util.List;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.enumeration.AdStatusEnum;
import com.round.aside.server.enumeration.AdStatusOpeEnum;
import com.round.aside.server.util.StringUtil;

/**
 * 审核广告Servlet所用的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public class CheckAdRequestPara extends UserIDTokenRequestPara {

    private final int adID;
    private final AdStatusOpeEnum adOpe;
    private final AdStatusEnum adFinalState;

    public CheckAdRequestPara(Builder mTBuilder) {
        super(mTBuilder.mUserIDTokenRPBuilder);

        adID = mTBuilder.mAdID;
        adOpe = mTBuilder.mAdOpe;
        adFinalState = mTBuilder.mAdFinalState;
    }

    public int getAdID() {
        return adID;
    }

    public AdStatusOpeEnum getAdOpe() {
        return adOpe;
    }

    public AdStatusEnum getAdFinalState() {
        return adFinalState;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            AbsRequestPara.AbsBuilder<CheckAdRequestPara> {

        private final UserIDTokenRequestPara.Builder mUserIDTokenRPBuilder;

        private int mAdID;
        private AdStatusOpeEnum mAdOpe;
        private AdStatusEnum mAdFinalState;

        public Builder() {
            super();

            mUserIDTokenRPBuilder = new UserIDTokenRequestPara.Builder();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);

            mUserIDTokenRPBuilder.onFillFieldKey(mParaSet);

            mParaSet.addKey("adID", true).addKey("adOpe", true);
            mParaSet.addKey("adFinalState", true);
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
                int mAdOpeIndex = mParaSet.getValueAsInt("adOpe");
                mAdOpe = AdStatusOpeEnum.valueOf(mAdOpeIndex);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad 操作码参数非法";
            }

            try {
                int mAdFinalStateIndex = mParaSet.getValueAsInt("adFinalState");
                mAdFinalState = AdStatusEnum.valueOf(mAdFinalStateIndex);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad 审核结果状态码参数非法";
            }

            return null;
        }

        @Override
        protected void onFillCombinationBuilder(
                List<AbsBuilder<? extends AbsRequestPara>> mTCombinationList) {
            super.onFillCombinationBuilder(mTCombinationList);
            mTCombinationList.add(mUserIDTokenRPBuilder);
        }

        @Override
        protected String onCheckAfterFillField() {
            String error = super.onCheckAfterFillField();
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            error = mUserIDTokenRPBuilder.onCheckAfterFillField();
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            if (mAdOpe != AdStatusOpeEnum.CHECK) {
                return "广告操作参数必须为审核操作";
            }

            if (mAdFinalState != AdStatusEnum.VALID
                    && mAdFinalState != AdStatusEnum.NOTPASS) {
                return "广告审核结果必须为审核通过或审核未通过";
            }

            return null;
        }

        @Override
        protected void onCheckBeforeBuild() throws IllegalStateException {
            super.onCheckBeforeBuild();

            mUserIDTokenRPBuilder.onCheckBeforeBuild();
        }

        @Override
        protected CheckAdRequestPara buildInstance() {
            return new CheckAdRequestPara(this);
        }

    }

}
