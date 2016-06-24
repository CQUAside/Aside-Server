package com.round.aside.server.module.accountmanager;

import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.statuscode.AuthCodeStatusCodeBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.bean.statuscode.UserEmailAuthStatusBean;
import com.round.aside.server.enumeration.UserEmailStatusEnum;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;
import com.round.aside.server.module.generator.IGenerator;
import com.round.aside.server.util.AssistUtils;
import com.round.aside.server.util.StringUtil;
import com.round.aside.server.util.VerifyUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 账号管理模块超级接口的实现类
 * 
 * @author A Shuai
 * 
 */
public final class AccountManagerImpl implements IAccountManager {

    @Override
    public StatusCodeBean checkRegisteredAccountLegal(String mAccount) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmpty(mAccount)) {
            return mBuilder.setStatusCode(ER5001).setMsg("账号参数非法").build();
        }

        Pattern mPattern = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9[_]]+)");
        Matcher mMatcher = mPattern.matcher(mAccount);
        if (!mMatcher.matches()) {
            return mBuilder.setStatusCode(R6001).setMsg("账号非法，请正确填写").build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        StatusCodeBean mStatusCodeBean = mDBManager
                .checkAccountExistence(mAccount);
        mDBManager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
            case R6002:
            case ER5001:
                mBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            case EX2016:
                mBuilder.setStatusCode(EX2010).setMsg(mStatusCodeBean.getMsg());
                break;
            default:
                throw new IllegalStateException("Illegal status code!");
        }
        return mBuilder.build();
    }

    @Override
    public StatusCodeBean sendPhoneAuthcode(String mPhone) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();
        if (StringUtil.isEmpty(mPhone)) {
            return mBuilder.setStatusCode(ER5001).setMsg("手机号输入为空，请正确输入")
                    .build();
        }
        if (!VerifyUtils.isPhoneNumber(mPhone)) {
            return mBuilder.setStatusCode(ER5002).setMsg("请输入合法的手机号").build();
        }

        String mAuthCode = AssistUtils.sendPhoneAuth(mPhone);
        if (StringUtil.isEmpty(mAuthCode) || mAuthCode.length() != 4) {
            return mBuilder.setStatusCode(R6003).setMsg("验证码发送失败，请重试").build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = mDBManager.stashRegisterPhoneAuthcode(
                mPhone, mAuthCode, System.currentTimeMillis() + 5 * 60 * 1000);
        mDBManager.release();

        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
            case ER5001:
                mBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            case EX2013:
                mBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }
        return mBuilder.build();
    }

    /**
     * 这里的业务逻辑首先向用户表插入一条账号注册记录，然后向token表插入一条登陆记录。这两部操作需要包装成一个事务。
     */
    @Override
    public UserIDTokenSCBean registerAccount(String mAccount, String mPassword,
            String mPhone, String mAuthcode, RequestInfoBean mRequestInfoBean) {
        UserIDTokenSCBean.Builder mUserBuilder = new UserIDTokenSCBean.Builder();

        if (StringUtil.isEmptyInSet(mAccount, mPassword, mPhone, mAuthcode)
                || mAuthcode.length() != 4) {
            mUserBuilder.setStatusCode(ER5001).setMsg("账号或密码输入非法，请检查后重新输入");
            return mUserBuilder.build();
        }

        if (!VerifyUtils.isPhoneNumber(mPhone)) {
            mUserBuilder.setStatusCode(ER5002).setMsg("注册手机号非法");
            return mUserBuilder.build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = mDBManager.checkRegisterPhoneAuthcode(
                mPhone, mAuthcode, System.currentTimeMillis());

        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
                mUserBuilder.setStatusCode(S1000).setMsg("手机验证码验证通过");
                break;
            case EX2016:
                mDBManager.release();
                mUserBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                return mUserBuilder.build();
            case ER5003L:
            case ER5004L:
                mDBManager.release();
                mUserBuilder.setStatusCodeBean(mStatusCodeBean);
                return mUserBuilder.build();
            case ER5001:
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(
                IGenerator.class, null);

        // 开始事务。若未开启成功，则立即返回报错
        if (!mDBManager.beginTransaction()) {
            mDBManager.closeTransaction();
            mDBManager.release();
            mGenerator.release();
            mUserBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
            return mUserBuilder.build();
        }

        int mUserID = -1;

        LOOP: while (true) {

            mUserID = mGenerator.generateUserID(0);
            mStatusCodeBean = mDBManager.insertUser(mUserID, mAccount,
                    mPassword, mPhone);
            switch (mStatusCodeBean.getStatusCode()) {
                case S1000:
                    mUserBuilder.setStatusCode(S1000).setMsg("账号写入成功");
                    break LOOP;
                case F8001:
                    // userid产生了冲突，需重新生成直至插入成功为止
                    mUserBuilder.setStatusCode(F8001).setMsg("UserID冲突");
                    continue;
                case F8002:
                    mUserBuilder.setStatusCode(F8003L).setMsg("账号重复");
                    break;
                case EX2013:
                    mUserBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                    break;
                case ER5001:
                    mUserBuilder.setStatusCode(ER5001).setMsg("参数非法");
                    break;
                default:
                    mDBManager.rollbackTransaction();
                    mDBManager.closeTransaction();
                    mDBManager.release();
                    mGenerator.release();
                    throw new IllegalStateException("Illegal Status Code!");
            }

            if (mStatusCodeBean.getStatusCode() != S1000) {
                mDBManager.rollbackTransaction();
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                return mUserBuilder.build();
            }

        }

        long mCuttentTime = System.currentTimeMillis();
        String mToken = mGenerator.generateToken(mUserID, mCuttentTime,
                mRequestInfoBean.getOS(), mRequestInfoBean.getBrowserName());
        mStatusCodeBean = mDBManager.insertToken(mUserID, mRequestInfoBean,
                mToken, mCuttentTime, mCuttentTime + 7 * 24 * 60 * 60 * 1000);

        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
                mUserBuilder.setStatusCode(S1000).setMsg("Token写入成功");
                break;
            case EX2013:
                mUserBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
                mUserBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                mDBManager.rollbackTransaction();
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        UserIDTokenSCBean mUserBean = mUserBuilder.build();

        if (mUserBean.getStatusCode() == S1000) {
            if (mDBManager.commitTransaction()) {
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                mUserBuilder.setStatusCode(S1000).setMsg("注册成功");
                mUserBuilder.setUserID(mUserID).setToken(mToken);
            } else {
                mDBManager.rollbackTransaction();
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                mUserBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
            }

        } else {
            mDBManager.rollbackTransaction();
            mDBManager.closeTransaction();
            mDBManager.release();
            mGenerator.release();
        }

        return mUserBuilder.build();
    }

    @Override
    public UserIDTokenSCBean login(String mAccount, String mPassword, long period,
            RequestInfoBean mRequestInfoBean) {
        UserIDTokenSCBean.Builder mUserBuilder = new UserIDTokenSCBean.Builder();

        if (StringUtil.isEmptyInSet(mAccount, mPassword) || period <= 0) {
            mUserBuilder.setStatusCode(ER5001).setMsg("账号或密码输入非法，请检查后重新输入");
            return mUserBuilder.build();
        }
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        UserIDTokenSCBean mLoginUserBean = mDBManager.loginCheck(mAccount,
                mPassword, period);
        switch (mLoginUserBean.getStatusCode()) {
            case S1000:
                mUserBuilder.setStatusCodeBean(mLoginUserBean).setMsg(
                        "账号密码验证通过");
                break;
            case R6004:
            case R6005:
            case ER5001:
                mDBManager.release();
                mUserBuilder.setStatusCodeBean(mLoginUserBean);
                return mUserBuilder.build();
            case EX2016:
                mDBManager.release();
                mUserBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                return mUserBuilder.build();
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(
                IGenerator.class, null);

        long mCuttentTime = System.currentTimeMillis();
        String mToken = mGenerator.generateToken(mLoginUserBean.getUserID(),
                mCuttentTime, mRequestInfoBean.getOS(),
                mRequestInfoBean.getBrowserName());
        StatusCodeBean mStatusCodeBean = mDBManager.insertToken(
                mLoginUserBean.getUserID(), mRequestInfoBean, mToken,
                mCuttentTime, mCuttentTime + period);
        mDBManager.release();
        mGenerator.release();

        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
                mUserBuilder.setStatusCode(S1000).setMsg("登陆成功");
                mUserBuilder.setToken(mToken);
                break;
            case EX2013:
                mUserBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
                mUserBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        return mUserBuilder.build();
    }

    @Override
    public StatusCodeBean verifyToken(int userId, String token) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmptyInSet(token)) {
            return mBuilder.setStatusCode(R6006).setMsg("Token非法").build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = mDBManager.tokenLoginCheck(userId,
                token);
        mDBManager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
            case R6006:
            case R6007:
            case ER5001:
                mBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            case EX2016:
                mBuilder.setStatusCode(EX2010).setMsg("数据库操作异常");
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        return mBuilder.build();
    }

    @Override
    public StatusCodeBean verifyAdminPermission(int mUserID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (mUserID < 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法");
            return mResultBuilder.build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mSCB = mDBManager.adminPermissionCheck(mUserID);
        mDBManager.release();
        switch (mSCB.getStatusCode()) {
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case S1000:
            case ER5001:
            case R6008:
            case ER5011:
                mResultBuilder.setStatusCodeBean(mSCB);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean activateEmail(int mUserID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (mUserID < 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        UserEmailAuthStatusBean mUserEmailAuthSCB = mDBManager
                .selectUserEmailStatus(mUserID);
        switch (mUserEmailAuthSCB.getStatusCode()) {
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case S1000:
                if (mUserEmailAuthSCB.getUserEmailStatus() == UserEmailStatusEnum.AUTHED) {
                    mResultBuilder.setStatusCode(R6013)
                            .setMsg("邮箱已验证通过，无需再次认证");
                } else {
                    mResultBuilder.setStatusCodeBean(mUserEmailAuthSCB);
                }
                break;
            case ER5001:
                mResultBuilder.setStatusCodeBean(mUserEmailAuthSCB);
                break;
            case R6008:
                mResultBuilder.setStatusCodeBean(mUserEmailAuthSCB).setMsg(
                        "无此UserID用户");
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        if (mUserEmailAuthSCB.getStatusCode() != S1000
                || mUserEmailAuthSCB.getUserEmailStatus() == UserEmailStatusEnum.AUTHED) {
            mDBManager.release();
            return mResultBuilder.build();
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(
                IGenerator.class, null);
        String mAuthCode = mGenerator.generateEmailAuthCode(System
                .currentTimeMillis());
        mGenerator.release();

        StatusCodeBean mStatusCodeBean = mDBManager.insertUserEmailAuthCode(
                mUserID, mUserEmailAuthSCB.getUserEmail(), mAuthCode,
                System.currentTimeMillis() + 5 * 60 * 1000);
        mDBManager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2013:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case S1000:
                mResultBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        if (mStatusCodeBean.getStatusCode() != S1000) {
            return mResultBuilder.build();
        }

        if (AssistUtils.sendActivationEmailAuthCode(
                mUserEmailAuthSCB.getUserEmail(), mAuthCode)) {
            mResultBuilder.setStatusCode(S1000).setMsg("认证码发送成功");
        } else {
            mResultBuilder.setStatusCode(R6009).setMsg("认证码邮件发送失败");
        }
        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean validateActivationEmail(int mUserID, String mEmail,
            String mAuthCode) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (mUserID <= 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }
        if (StringUtil.isEmpty(mEmail)) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("邮箱地址参数非法")
                    .build();
        }
        if (StringUtil.isEmpty(mAuthCode) || mAuthCode.length() != 4) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("认证码参数非法")
                    .build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        AuthCodeStatusCodeBean mAuthCodeSCB = mDBManager.getUserEmailAuthCode(
                mUserID, mEmail);

        switch (mAuthCodeSCB.getStatusCode()) {
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case S1000:
            case ER5001:
            case R6008:
                mResultBuilder.setStatusCodeBean(mAuthCodeSCB);
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }
        if (mAuthCodeSCB.getStatusCode() != S1000) {
            mDBManager.release();
            return mResultBuilder.build();
        }

        boolean mAuth = mAuthCodeSCB.getAuthCode().equals(mAuthCode);
        StatusCodeBean mStatusCodeBean = mDBManager.updateUserEmailStatus(
                mUserID, mAuth ? UserEmailStatusEnum.AUTHED
                        : UserEmailStatusEnum.AUTHLESS);
        mDBManager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2014:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case S1000:
                mResultBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        if (mStatusCodeBean.getStatusCode() == S1000) {
            if (mAuth) {
                mResultBuilder.setStatusCode(S1000).setMsg("认证成功");
            } else {
                mResultBuilder.setStatusCode(R6010).setMsg("认证失败");
            }
        }

        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean retrievePassword(int mUserID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (mUserID <= 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        UserEmailAuthStatusBean mUserEmailAuthSCB = mDBManager
                .selectUserEmailStatus(mUserID);

        switch (mUserEmailAuthSCB.getStatusCode()) {
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case S1000:
                if (mUserEmailAuthSCB.getUserEmailStatus() == UserEmailStatusEnum.AUTHLESS) {
                    mResultBuilder.setStatusCode(R6012).setMsg(
                            "邮箱认证失败，不能用于寻回密码");
                } else if (mUserEmailAuthSCB.getUserEmailStatus() == UserEmailStatusEnum.UNAUTH) {
                    mResultBuilder.setStatusCode(R6011).setMsg(
                            "邮箱尚未认证，不能用于寻回密码");
                } else {
                    mResultBuilder.setStatusCodeBean(mUserEmailAuthSCB);
                }
                break;
            case ER5001:
            case R6008:
                mResultBuilder.setStatusCodeBean(mUserEmailAuthSCB);
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        if (mUserEmailAuthSCB.getStatusCode() != S1000
                || mUserEmailAuthSCB.getUserEmailStatus() != UserEmailStatusEnum.AUTHED) {
            mDBManager.release();
            return mResultBuilder.build();
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(
                IGenerator.class, null);
        String mAuthCode = mGenerator.generateRetrieverPasswordAuthCode(System
                .currentTimeMillis());
        mGenerator.release();

        StatusCodeBean mStatusCodeBean = mDBManager
                .insertRetrieverPasswordAuthCode(mUserID,
                        mUserEmailAuthSCB.getUserEmail(), mAuthCode,
                        System.currentTimeMillis() + 5 * 60 * 1000);
        mDBManager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2013:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case S1000:
                mResultBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        if (mStatusCodeBean.getStatusCode() != S1000) {
            return mResultBuilder.build();
        }

        if (AssistUtils.sendRetrieverPasswordAuthCode(
                mUserEmailAuthSCB.getUserEmail(), mAuthCode)) {
            mResultBuilder.setStatusCode(S1000).setMsg("密码找回邮件发送成功");
        } else {
            mResultBuilder.setStatusCode(R6009).setMsg("密码找回邮件发送失败");
        }
        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean validationRetrieverPassword(int mUserID,
            String mEmail, String mAuthCode) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (mUserID <= 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }
        if (StringUtil.isEmpty(mEmail)) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("邮箱地址参数非法")
                    .build();
        }
        if (StringUtil.isEmpty(mAuthCode) || mAuthCode.length() != 4) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("认证码参数非法")
                    .build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        AuthCodeStatusCodeBean mAuthCodeSCB = mDBManager.getUserEmailAuthCode(
                mUserID, mEmail);
        mDBManager.release();

        switch (mAuthCodeSCB.getStatusCode()) {
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case S1000:
            case ER5001:
            case R6008:
                mResultBuilder.setStatusCodeBean(mAuthCodeSCB);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }
        if (mAuthCodeSCB.getStatusCode() == S1000) {
            if (mAuthCodeSCB.getAuthCode().equals(mAuthCode)) {
                mResultBuilder.setStatusCode(S1000).setMsg("认证成功，准许修改密码");
            } else {
                mResultBuilder.setStatusCode(R6010).setMsg("认证码不符，不可修改密码");
            }
        }

        return mResultBuilder.build();
    }
}
