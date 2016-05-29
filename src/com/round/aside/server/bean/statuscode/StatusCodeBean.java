package com.round.aside.server.bean.statuscode;

/**
 * 状态码的数据bean，包含了状态码以及描述信息
 * 
 * @author A Shuai
 * @date 2016-5-25
 * 
 */
public class StatusCodeBean {

    private final int statusCode;
    private final String msg;

    public StatusCodeBean(Builder mBuilder) {
        this.statusCode = mBuilder.statusCode;
        this.msg = mBuilder.msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-26
     * 
     */
    public static class Builder {

        private int statusCode;
        private String msg;

        public Builder() {
            statusCode = 0;
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setStatusCodeBean(StatusCodeBean mBean) {
            this.statusCode = mBean.statusCode;
            this.msg = mBean.msg;
            return this;
        }

        public StatusCodeBean build() {
            if (statusCode == 0) {
                throw new IllegalStateException("status code must be assign!");
            }
            return new StatusCodeBean(this);
        }

    }

}
