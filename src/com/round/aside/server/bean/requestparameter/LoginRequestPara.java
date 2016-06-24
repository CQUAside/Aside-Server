package com.round.aside.server.bean.requestparameter;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * Login登陆Servlet所对应的参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public final class LoginRequestPara extends AbsRequestPara {

    private final String account;
    private final String password;

    public LoginRequestPara(Builder mTBuilder) {
        super();

        account = mTBuilder.mAccount;
        password = mTBuilder.mPassword;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            AbsRequestPara.AbsBuilder<LoginRequestPara> {

        private String mAccount;
        private String mPassword;

        public Builder() {
            super();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);

            mParaSet.addKey("account", true).addKey("password", true);
        }

        @Override
        protected String onFillField(RequestParameterSet mParaSet) {
            String error = super.onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            mAccount = mParaSet.getValue("account");
            mPassword = mParaSet.getValue("password");

            return null;
        }

        @Override
        protected LoginRequestPara buildInstance() {
            return new LoginRequestPara(this);
        }

    }

}
