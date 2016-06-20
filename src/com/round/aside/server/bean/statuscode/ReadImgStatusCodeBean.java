package com.round.aside.server.bean.statuscode;

/**
 * 读取图像状态码数据bean
 * 
 * @author A Shuai
 * @date 2016-6-17
 * 
 */
public class ReadImgStatusCodeBean extends StatusCodeBean {

    private final String mBase64ImgData;

    public ReadImgStatusCodeBean(Builder mBuilder) {
        super(mBuilder);
        this.mBase64ImgData = mBuilder.mBase64ImgData;
    }

    /**
     * 读取经过Base64编码后的图像数据
     * 
     * @return 经过Base64编码的图像数据
     */
    public String getBase64ImgData() {
        return mBase64ImgData;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-17
     *
     */
    public static class Builder extends StatusCodeBean.Builder {

        private String mBase64ImgData;

        public Builder() {
        }

        public Builder setBase64ImgData(String mTBase64ImgData) {
            this.mBase64ImgData = mTBase64ImgData;
            return this;
        }

        @Override
        public ReadImgStatusCodeBean build() {
            check();
            return new ReadImgStatusCodeBean(this);
        }

    }

}
