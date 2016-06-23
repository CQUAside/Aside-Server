package com.round.aside.server.bean.requestparameter;

/**
 * 获取定位城市Servlet对应的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public class GetLocateCityRequestPara extends UserIDTokenRequestPara {

    public GetLocateCityRequestPara(Builder mTBuilder) {
        super(mTBuilder);
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            UserIDTokenRequestPara.Builder<GetLocateCityRequestPara> {

        public Builder() {
            super();
        }

        @Override
        protected GetLocateCityRequestPara buildInstance() {
            return new GetLocateCityRequestPara(this);
        }

    }

}
