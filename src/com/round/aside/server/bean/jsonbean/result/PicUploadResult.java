package com.round.aside.server.bean.jsonbean.result;

/**
 * 单张图片上传的结果数据bean
 * 
 * @author A Shuai
 * @date 2016-5-25
 * 
 */
public final class PicUploadResult {

    private final String picName;
    private final int ordinal;
    private final String picID;

    public PicUploadResult(Builder mBuilder) {
        this.picName = mBuilder.picName;
        this.ordinal = mBuilder.ordinal;
        this.picID = mBuilder.picID;
    }

    public String getPicName() {
        return picName;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getPicID() {
        return picID;
    }


    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-25
     * 
     */
    public static class Builder {

        private String picName;
        private int ordinal;
        private String picID;

        public Builder() {
        }

        public Builder setPicName(String picName) {
            this.picName = picName;
            return this;
        }

        public Builder setOrdinal(int ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder setPicID(String picID) {
            this.picID = picID;
            return this;
        }

        public PicUploadResult build() {
            return new PicUploadResult(this);
        }

    }

}
