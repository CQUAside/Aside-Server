package com.round.aside.server.bean.requestparameter;

/**
 * 图片上传Servlet对应的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public class PicUploadRequestPara extends UserIDTokenRequestPara {

    public PicUploadRequestPara(Builder mTBuilder) {
        super(mTBuilder.mUserIDTokenRPBuilder);
    }

    public static class Builder extends
            AbsRequestPara.AbsBuilder<PicUploadRequestPara> {

        private final UserIDTokenRequestPara.Builder mUserIDTokenRPBuilder;

        public Builder() {
            super();

            mUserIDTokenRPBuilder = new UserIDTokenRequestPara.Builder();
        }

        @Override
        protected PicUploadRequestPara buildInstance() {
            return new PicUploadRequestPara(this);
        }

    }

}
