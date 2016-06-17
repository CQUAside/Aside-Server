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
        super(mBuilder);
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
    public static class Builder extends StatusCodeBean.Builder {

        private int sum;

        public Builder() {
        }

        public Builder setSum(int sum) {
            if (sum < 0) {
                throw new IllegalArgumentException(
                        "the count sum shouldn't be negative!");
            }
            this.sum = sum;
            return this;
        }

        public IncrementSumStatusCodeBean build() {
            check();
            return new IncrementSumStatusCodeBean(this);
        }

    }

}
