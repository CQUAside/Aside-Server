package com.round.aside.server.bean;

/**
 * 浏览器请求中所附带的相关参数。如操作系统及版本号，浏览器及版本号等。建造者模式。
 * 
 * @author A Shuai
 * @date 2016-4-29
 * 
 */
public final class RequestInfoBean {

    private final String mOS;
    private final String mBrowName;
    private final String mBrowNameAndVer;

    public RequestInfoBean(Builder mBuilder) {
        this.mOS = mBuilder.mOS;
        this.mBrowName = mBuilder.mBrowName.toString();
        this.mBrowNameAndVer = mBuilder.mBrowNameAndVer.toString();
    }

    public String getOS() {
        return mOS;
    }

    public String getBrowserName() {
        return mBrowName;
    }

    public String getBrowNameAndVer() {
        return mBrowNameAndVer;
    }


    /**
     * 建造者类，静态内部类。
     * 
     * @author A Shuai
     * @date 2016-4-29
     * 
     */
    public static class Builder {

        private String mOS;
        private final StringBuilder mBrowName;
        private final StringBuilder mBrowNameAndVer;

        public Builder() {
            mBrowName = new StringBuilder();
            mBrowNameAndVer = new StringBuilder();
        }

        public Builder setOS(String mOS) {
            this.mOS = mOS;
            return this;
        }

        /**
         * 追加浏览器参数信息，包括名字和版本号
         * 
         * @param mBroName
         * @param mBroVersion
         * @return
         */
        public Builder appendBrowser(String mBroName, String mBroVersion) {
            mBrowName.append(mBroName).append(';');
            mBrowNameAndVer.append(mBroName).append('/').append(mBroVersion).append(';');
            return this;
        }

        public RequestInfoBean build() {
            if (mBrowName.length() > 0) {
                mBrowName.deleteCharAt(mBrowName.length() - 1);
                mBrowNameAndVer.deleteCharAt(mBrowNameAndVer.length() - 1);
            }
            return new RequestInfoBean(this);
        }

    }

}
