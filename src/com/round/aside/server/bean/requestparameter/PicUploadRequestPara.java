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
        super(mTBuilder);
    }

    public static class Builder extends
            UserIDTokenRequestPara.Builder<PicUploadRequestPara> {

        public Builder() {
            super();
        }

        @Override
        protected PicUploadRequestPara buildInstance() {
            return new PicUploadRequestPara(this);
        }

    }

}
