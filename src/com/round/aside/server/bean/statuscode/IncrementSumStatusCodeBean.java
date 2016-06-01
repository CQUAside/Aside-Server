package com.round.aside.server.bean.statuscode;

/**
 * 增量和状态码数据bean
 * 
 * @author A Shuai
 * @date 2016-6-1
 *
 */
public class IncrementSumStatusCodeBean extends StatusCodeBean {

    private final int sum;

    public IncrementSumStatusCodeBean(Builder mBuilder) {
        super(mBuilder.mPBuilder);
        sum = mBuilder.sum;
    }

    public int getSum() {
        return sum;
    }


    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-1
     * 
     */
    public static class Builder {

        private int sum;
        private final StatusCodeBean.Builder mPBuilder;

        public Builder() {
            mPBuilder = new StatusCodeBean.Builder();
        }

        public Builder setSum(int sum) {
            if (sum < 0) {
                throw new IllegalArgumentException(
                        "the count sum shouldn't be negative!");
            }
            this.sum = sum;
            return this;
        }

        public Builder setStatusCodeBean(StatusCodeBean statusCodeBean) {
            mPBuilder.setStatusCode(statusCodeBean.getStatusCode());
            mPBuilder.setMsg(statusCodeBean.getMsg());
            return this;
        }

        public Builder setStatusCode(int statusCode) {
            mPBuilder.setStatusCode(statusCode);
            return this;
        }

        public Builder setMsg(String msg) {
            mPBuilder.setMsg(msg);
            return this;
        }

        public IncrementSumStatusCodeBean build() {
            return new IncrementSumStatusCodeBean(this);
        }

    }

}
