package com.round.aside.server.bean.requestparameter;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * 获取定位城市Servlet对应的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public class GetLocateCityRequestPara extends UserIDTokenRequestPara {

    public GetLocateCityRequestPara(Builder mTBuilder) {
        super(mTBuilder.mUserIDTokenRPBuilder);
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            AbsRequestPara.AbsBuilder<GetLocateCityRequestPara> {

        private final UserIDTokenRequestPara.Builder mUserIDTokenRPBuilder;

        public Builder() {
            super();

            mUserIDTokenRPBuilder = new UserIDTokenRequestPara.Builder();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);
            mUserIDTokenRPBuilder.onFillFieldKey(mParaSet);
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
        protected GetLocateCityRequestPara buildInstance() {
            return new GetLocateCityRequestPara(this);
        }

    }

}
