package com.round.aside.server.bean.requestparameter;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * 仅包含AdID的Servlet的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-23
 * 
 */
public class AdIDRequestPara extends AbsRequestPara {

    private final int adID;

    public AdIDRequestPara(Builder mTBuilder) {
        super();

        adID = mTBuilder.mAdID;
    }

    public int getAdID() {
        return adID;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-23
     * 
     */
    public static class Builder extends
            AbsRequestPara.AbsBuilder<AdIDRequestPara> {

        private int mAdID;

        public Builder() {
            super();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);

            mParaSet.addKey("adID", true);
        }

        @Override
        protected String onFillField(RequestParameterSet mParaSet) {
            String error = super.onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            try {
                mAdID = mParaSet.getValueAsInt("adID");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "Ad ID参数非法";
            }

            return null;
        }

        @Override
        protected AdIDRequestPara buildInstance() {
            return new AdIDRequestPara(this);
        }

    }

}
