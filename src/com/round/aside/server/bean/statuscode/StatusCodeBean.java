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

        /**
         * 检查函数<br>
         * 各子类根据情况进行覆写，完成新增字段的检查。最后在调用build方法完成构建之前请务必调用此方法进行一次检查。<br>
         * 子类覆写时请务必向上调用父类的方法体，保证父类的检查。
         */
        protected void check() {
            if (statusCode == 0) {
                throw new IllegalStateException("status code must be assign!");
            }
        }

        public StatusCodeBean build() {
            check();
            return new StatusCodeBean(this);
        }

    }

}
