package com.round.aside.server.bean.requestparameter;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.enumeration.AdStatusEnum;
import com.round.aside.server.enumeration.AdStatusOpeEnum;
import com.round.aside.server.util.StringUtil;

/**
 * 审核广告Servlet所用的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-21
 * 
 */
public class CheckAdServletRequestPara {

    private final int adID;
    private final AdStatusOpeEnum adOpe;
    private final AdStatusEnum adFinalState;

    public CheckAdServletRequestPara(Builder mTBuilder) {
        adID = mTBuilder.mAdID;
        adOpe = mTBuilder.mAdOpe;
        adFinalState = mTBuilder.mAdFinalState;
    }

    public int getAdID() {
        return adID;
    }

    public AdStatusOpeEnum getAdOpe() {
        return adOpe;
    }

    public AdStatusEnum getAdFinalState() {
        return adFinalState;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-21
     * 
     */
    public static class Builder {

        private boolean init;

        private int mAdID;
        private AdStatusOpeEnum mAdOpe;
        private AdStatusEnum mAdFinalState;

        public Builder() {
            init = false;
        }

        /**
         * 填充字段
         * 
         * @param mParaSet
         *            参数集
         * @return 成功返回null，失败返回错误信息
         */
        public String fillField(RequestParameterSet mParaSet) {
            String mAdIDStr = mParaSet.getValue("adID");
            try {
                mAdID = Integer.parseInt(mAdIDStr);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad ID参数非法";
            }

            String mAdOpeIndexStr = mParaSet.getValue("adOpe");
            try {
                int mAdOpeIndex = Integer.parseInt(mAdOpeIndexStr);
                mAdOpe = AdStatusOpeEnum.valueOf(mAdOpeIndex);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad 操作码参数非法";
            }

            String mAdFinalStateIndexStr = mParaSet.getValue("adFinalState");
            try {
                int mAdFinalStateIndex = Integer
                        .parseInt(mAdFinalStateIndexStr);
                mAdFinalState = AdStatusEnum.valueOf(mAdFinalStateIndex);
            } catch (Exception e) {
                e.printStackTrace();
                return "Ad 审核结果状态码参数非法";
            }

            String error = checkAfterFillField();
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            init = true;
            return null;
        }

        /**
         * 字段域填充完毕后的检查<br>
         * 用于检查字段赋值的不合法填充
         * 
         * @return 检查通过返回null，检查失败返回错误原因
         */
        protected String checkAfterFillField() {
            if (mAdOpe != AdStatusOpeEnum.CHECK) {
                return "广告操作参数必须为审核状态";
            }

            if (mAdFinalState != AdStatusEnum.VALID
                    && mAdFinalState != AdStatusEnum.NOTPASS) {
                return "广告审核结果必须为审核通过或审核未通过";
            }

            return null;
        }

        protected void checkBeforeBuild() throws IllegalStateException {
            if (!init) {
                throw new IllegalStateException(
                        "the builder of operate ad entity must be inited!");
            }
        }

        public CheckAdServletRequestPara build() {
            checkBeforeBuild();
            return new CheckAdServletRequestPara(this);
        }

    }

}
