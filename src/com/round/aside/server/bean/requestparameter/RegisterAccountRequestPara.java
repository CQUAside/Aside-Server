package com.round.aside.server.bean.requestparameter;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * Register注册Servlet所用的参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public class RegisterAccountRequestPara extends AbsRequestPara {

    private final String account;
    private final String password;
    private final String phone;
    private final String authcode;

    public RegisterAccountRequestPara(Builder mTBuilder) {
        super();

        account = mTBuilder.mAccount;
        password = mTBuilder.mPassword;
        phone = mTBuilder.mPhone;
        authcode = mTBuilder.mAuthcode;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAuthcode() {
        return authcode;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            AbsRequestPara.AbsBuilder<RegisterAccountRequestPara> {

        private String mAccount;
        private String mPassword;
        private String mPhone;
        private String mAuthcode;

        public Builder() {
            super();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);

            mParaSet.addKey("account", true).addKey("password", true);
            mParaSet.addKey("phone", true).addKey("authcode", true);
        }

        @Override
        protected String onFillField(RequestParameterSet mParaSet) {
            String error = super.onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            mAccount = mParaSet.getValue("account");
            mPassword = mParaSet.getValue("password");
            mPhone = mParaSet.getValue("phone");
            mAuthcode = mParaSet.getValue("authcode");

            return null;
        }

        @Override
        protected RegisterAccountRequestPara buildInstance() {
            return new RegisterAccountRequestPara(this);
        }

    }

}
