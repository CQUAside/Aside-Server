package com.round.aside.server.bean.jsonbean;

import com.round.aside.server.bean.statuscode.StatusCodeBean;

/**
 * 基础JSON结果数据bean
 * 
 * @author A Shuai
 * @date 2016-5-4
 *
 */
public final class BaseResultBean {

    private final int statusCode;
    private final String msg;
    private final Object obj;

    public BaseResultBean(Builder mBuilder) {
        statusCode = mBuilder.statusCode;
        msg = mBuilder.msg;
        obj = mBuilder.obj;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public Object getObj() {
        return obj;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-4
     *
     */
    public static class Builder {

        private int statusCode;
        private String msg;
        private Object obj;

        public Builder() {
            statusCode = 0;
        }
        
        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        /**
         * 单独重新设置Msg
         * 
         * @param msg
         *            要设置的描述信息
         * @return
         */
        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setStatusCodeBean(StatusCodeBean mBean) {
            statusCode = mBean.getStatusCode();
            msg = mBean.getMsg();
            return this;
        }

        public Builder setObj(Object obj) {
            this.obj = obj;
            return this;
        }

        public BaseResultBean build() {
            if(statusCode == 0){
                throw new IllegalStateException("the statuscode must be init!");
            }
            return new BaseResultBean(this);
        }

    }

}