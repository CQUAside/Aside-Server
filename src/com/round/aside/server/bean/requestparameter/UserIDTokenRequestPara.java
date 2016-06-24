package com.round.aside.server.bean.requestparameter;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * 包含UserID和Token的基础请求参数集，便于Post类型请求二次包装。
 * 
 * @author A Shuai
 * @date 2016-6-21
 * 
 */
public class UserIDTokenRequestPara extends AbsRequestPara {

    private final int userID;
    private final String token;

    public UserIDTokenRequestPara(Builder mTBuilder) {
        super();

        userID = mTBuilder.mUserID;
        token = mTBuilder.mToken;
    }

    public int getUserID() {
        return userID;
    }

    public String getToken() {
        return token;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            AbsRequestPara.AbsBuilder<UserIDTokenRequestPara> {

        private int mUserID;
        private String mToken;

        public Builder() {
            super();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);

            mParaSet.addKey("userid", true).addKey("token", true);
        }

        @Override
        protected String onFillField(RequestParameterSet mParaSet) {
            String error = super.onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            try {
                mUserID = mParaSet.getValueAsInt("userid");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "UserID参数非法";
            }

            mToken = mParaSet.getValue("token");

            return null;
        }

        @Override
        protected UserIDTokenRequestPara buildInstance() {
            return new UserIDTokenRequestPara(this);
        }

    }

}
