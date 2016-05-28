package com.round.aside.server.module.accountmanager;

import com.round.aside.server.bean.LoginUserBean;
import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.StatusCodeBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;
import com.round.aside.server.module.generator.IGenerator;
import com.round.aside.server.util.AssistUtils;
import com.round.aside.server.util.StringUtil;
import com.round.aside.server.util.VerifyUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.*;

import static com.round.aside.server.constant.StatusCode.*;
import static com.round.aside.server.constant.Constants.*;

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
                mBuilder.setStatusCode(EX2000).setMsg(mStatusCodeBean.getMsg());
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
                mBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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
    public LoginUserBean registerAccount(String mAccount, String mPassword,
            String mPhone, String mAuthcode, RequestInfoBean mRequestInfoBean) {
        LoginUserBean.Builder mUserBuilder = new LoginUserBean.Builder();

        if (StringUtil.isEmptyInSet(mAccount, mPassword, mPhone, mAuthcode)
                || mAuthcode.length() != 4) {
            return mUserBuilder.setStatusCode(ER5001)
                    .setMsg("账号或密码输入非法，请检查后重新输入").build();
        }

        if (!VerifyUtils.isPhoneNumber(mPhone)) {
            return mUserBuilder.setStatusCode(ER5002).setMsg("注册手机号非法").build();
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
                return mUserBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试")
                        .build();
            case ER5003L:
            case ER5004L:
                mDBManager.release();
                return mUserBuilder.setStatusCodeBean(mStatusCodeBean).build();
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
            return mUserBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试")
                    .build();
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
                    mUserBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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
                mUserBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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

        LoginUserBean mUserBean = mUserBuilder.build();

        if (mUserBean.getStatusCode() == S1000) {
            if (mDBManager.commitTransaction()) {
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                mUserBuilder.setStatusCode(S1000).setMsg("注册成功")
                        .setUserID(mUserID).setToken(mToken);
            } else {
                mDBManager.rollbackTransaction();
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                mUserBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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
    public LoginUserBean login(String mAccount, String mPassword, long period,
            RequestInfoBean mRequestInfoBean) {
        LoginUserBean.Builder mUserBuilder = new LoginUserBean.Builder();

        if (StringUtil.isEmptyInSet(mAccount, mPassword) || period <= 0) {
            return mUserBuilder.setStatusCode(ER5001)
                    .setMsg("账号或密码输入非法，请检查后重新输入").build();
        }
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        LoginUserBean mLoginUserBean = mDBManager.loginCheck(mAccount,
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
                mUserBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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

        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
                mUserBuilder.setStatusCode(S1000).setMsg("登陆成功")
                        .setToken(mToken);
                break;
            case EX2013:
                mUserBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
                mUserBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                mDBManager.release();
                mGenerator.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        mDBManager.release();
        mGenerator.release();
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
                mBuilder.setStatusCode(EX2000).setMsg("数据库操作异常");
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        return mBuilder.build();
    }

    @Override
    public boolean activationEmail(int mUserID, String email) {
        int mStatusCode;
        int mStatus;
        String to = email;
        String smtp = "smtp.sina.cn";
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        mStatus = mDBManager.selectUserStatus(mUserID);// 获取用户邮箱的状态码，看是否以验证过；
        switch (mStatus) {
            case 0:
                System.out.print("邮箱已经验证过了");
            case S1000:
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }
        String registerID = "" + Math.random() * Math.random();
        mStatusCode = mDBManager.updateUserRegister(mUserID, registerID);
        mDBManager.release();

        if (mStatusCode != S1000) {
            return false;
        }
        // 获得系统属性
        Properties properties = System.getProperties();
        // 设置邮件主机
        properties.setProperty("mail.smtp.host", smtp);
        properties.setProperty("mail.smtp.auth", "true");
        // 设置我方网站地址，用户在点击后，返回本站
        String url = " ?registerID=" + registerID;
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(SEND_EMAIL_ADDRESS));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    to));

            // Set Subject: header field
            message.setSubject("网站激活");

            // Now set the actual message
            message.setText("This is actual message");
            message.setContent("<a href= url  ></a>点击下面，完成注册</br>" + url,
                    "text/html;charset=utf-8");
            // Send message
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(smtp, SEND_EMAIL_ADDRESS, SEND_EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean validationctivationEmail(int mUserID, String VerificationCode) {
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        if (mDBManager.selectUserRegister(mUserID, VerificationCode) == S1000) {
            mDBManager.updateUserStatus(mUserID);
            mDBManager.release();
            return true;
        } else {
            mDBManager.release();
            return false;
        }
    }

    @Override
    public boolean findPassword(int mUserID) {
        String email = "";
        String smtp = "smtp.sina.cn";
        int mStatusCode;
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        if (mDBManager.selectUserStatus(mUserID) != 1) {
            System.out.print("你还没有激活邮箱");
            mDBManager.release();
            return false;
        }
        email = mDBManager.selectUserEmail(mUserID);
        Properties properties = System.getProperties();
        // 设置邮件主机
        properties.setProperty("mail.smtp.host", smtp);
        properties.setProperty("mail.smtp.auth", "true");
        // 设置我方地址，用户在点击后，返回本站
        String randomTemp = "" + (Math.random() * Math.random() * 100000);
        String[] registerIDTemp = randomTemp.split(".");
        String registerID = registerIDTemp[0];
        mStatusCode = mDBManager.updateUserRegister(mUserID, registerID);
        mDBManager.release();
        if (mStatusCode != S1000) {
            return false;
        }
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(SEND_EMAIL_ADDRESS));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    email));

            // Set Subject: header field
            message.setSubject("网站激活");

            // Now set the actual message
            message.setText("This is actual message");
            message.setContent("<h2>下面是你的验证码</h2></br>" + registerID,
                    "text/html;charset=utf-8");
            // Send message
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(smtp, SEND_EMAIL_ADDRESS, SEND_EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean validationFindPassword(int mUserID, String verificationCode) {// 代码与验证激活邮箱是相同的，可以只用一个。
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        if (mDBManager.selectUserRegister(mUserID, verificationCode) == S1000) {
            mDBManager.updateUserStatus(mUserID);
            mDBManager.release();
            return true;
        } else {
            mDBManager.release();
            return false;
        }
    }
}
