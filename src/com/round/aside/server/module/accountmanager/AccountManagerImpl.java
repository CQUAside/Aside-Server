package com.round.aside.server.module.accountmanager;

import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.entity.LoginUserEntity;
import com.round.aside.server.entity.RegisterResultEntity;
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
    public int checkRegisteredAccountLegal(String mAccount) {
        if (StringUtil.isEmpty(mAccount)) {
            return ER5001;
        }

        Pattern mPattern = Pattern.compile("([a-zA-Z]+[a-zA-Z0-9[_]]+)");
        Matcher mMatcher = mPattern.matcher(mAccount);
        if (!mMatcher.matches()) {
            return R6001;
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        int mStatusCode = mDBManager.checkAccountExistence(mAccount);
        mDBManager.release();
        switch (mStatusCode) {
            case S1000:
            case R6002:
            case ER5001:
                return mStatusCode;
            case EX2012:
            case EX2013:
                return S1001;
            default:
                throw new IllegalStateException("Illegal status code!");
        }
    }

    @Override
    public int sendPhoneAuthcode(String mPhone) {
        if (StringUtil.isEmpty(mPhone)) {
            return ER5001;
        }
        if (!VerifyUtils.isPhoneNumber(mPhone)) {
            return ER5002;
        }

        String mAuthCode = AssistUtils.sendPhoneAuth(mPhone);
        if (StringUtil.isEmpty(mAuthCode) || mAuthCode.length() != 4) {
            return R6003;
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int mStatusCode = mDBManager.stashRegisterPhoneAuthcode(mPhone,
                mAuthCode, System.currentTimeMillis() + 5 * 60 * 1000);
        mDBManager.release();

        switch (mStatusCode) {
            case S1000:
            case ER5001:
                return mStatusCode;
            case EX2012:
            case EX2013:
                return R6003;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

    }

    /**
     * 这里的业务逻辑首先向用户表插入一条账号注册记录，然后向token表插入一条登陆记录。这两部操作需要包装成一个事务。
     */
    @Override
    public RegisterResultEntity registerAccount(String mAccount,
            String mPassword, String mPhone, String mAuthcode,
            RequestInfoBean mRequestInfoBean) {
        if (StringUtil.isEmptyInSet(mAccount, mPassword, mPhone, mAuthcode)
                || mAuthcode.length() != 4) {
            return new RegisterResultEntity(ER5001);
        }

        if (!VerifyUtils.isPhoneNumber(mPhone)) {
            return new RegisterResultEntity(ER5002);
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int mStatusCode = mDBManager.checkRegisterPhoneAuthcode(mPhone,
                mAuthcode, System.currentTimeMillis());

        switch (mStatusCode) {
            case S1000:
                break;
            case EX2016:
                mStatusCode = EX2000;
            case ER5003L:
            case ER5004L:
                mDBManager.release();
                return new RegisterResultEntity(mStatusCode);
            case ER5001:
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(
                IGenerator.class, null);

        // 开始事务。若未开启成功，则立即返回报错
        if (!mDBManager.beginTransaction()) {
            mDBManager.release();
            mGenerator.release();
            return new RegisterResultEntity(EX2000);
        }

        int mUserID = -1;

        LOOP: while (true) {

            mUserID = mGenerator.generateUserID(0);
            mStatusCode = mDBManager.insertUser(mUserID, mAccount, mPassword,
                    mPhone);
            switch (mStatusCode) {
                case S1000:
                    break LOOP;
                case F8001:
                    // userid产生了冲突，需重新生成直至插入成功为止
                    continue;
                case F8002:
                    mStatusCode = F8003L;
                    break;
                case EX2013:
                    mStatusCode = EX2000;
                    break;
                case ER5001:
                    mStatusCode = ER5001;
                    break;
                default:
                    mDBManager.rollbackTransaction();
                    mDBManager.closeTransaction();
                    mDBManager.release();
                    mGenerator.release();
                    throw new IllegalStateException("Illegal Status Code!");
            }

            if (mStatusCode != S1000) {
                mDBManager.rollbackTransaction();
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                return new RegisterResultEntity(mStatusCode);
            }

        }

        long mCuttentTime = System.currentTimeMillis();
        String mToken = mGenerator.generateToken(mUserID, mCuttentTime,
                mRequestInfoBean.getOS(), mRequestInfoBean.getBrowserName());
        mStatusCode = mDBManager.insertToken(mUserID, mRequestInfoBean, mToken,
                mCuttentTime, mCuttentTime + 7 * 24 * 60 * 60 * 1000);

        switch (mStatusCode) {
            case S1000:
                break;
            case EX2013:
                mStatusCode = EX2000;
                break;
            case ER5001:
                mStatusCode = ER5001;
                break;
            default:
                mDBManager.rollbackTransaction();
                mDBManager.closeTransaction();
                mDBManager.release();
                mGenerator.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        if (mStatusCode == S1000) {
            mDBManager.closeTransaction();
            mDBManager.release();
            mGenerator.release();
            return new RegisterResultEntity(mStatusCode, mUserID, mToken);
        } else {
            mDBManager.rollbackTransaction();
            mDBManager.closeTransaction();
            mDBManager.release();
            mGenerator.release();
            return new RegisterResultEntity(mStatusCode);
        }

    }

    @Override
    public LoginUserEntity login(String mAccount, String mPassword,
            long period, RequestInfoBean mRequestInfoBean) {
        if (StringUtil.isEmptyInSet(mAccount, mPassword) || period <= 0) {
            return new LoginUserEntity.Builder().setStatuscode(ER5001).build();
        }
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        LoginUserEntity.Builder mUserEntityBuilder;

        mUserEntityBuilder = mDBManager.login(mAccount, mPassword, period);
        switch (mUserEntityBuilder.getStatuscode()) {
            case S1000:
                break;
            case R6004:
            case R6005:
            case ER5001:
                mDBManager.release();
                return mUserEntityBuilder.build();
            case EX2016:
                mDBManager.release();
                mUserEntityBuilder.setStatuscode(EX2000);
                return mUserEntityBuilder.build();
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(
                IGenerator.class, null);

        long mCuttentTime = System.currentTimeMillis();
        String mToken = mGenerator.generateToken(
                mUserEntityBuilder.getUserID(), mCuttentTime,
                mRequestInfoBean.getOS(), mRequestInfoBean.getBrowserName());
        int mStatusCode = mDBManager.insertToken(
                mUserEntityBuilder.getUserID(), mRequestInfoBean, mToken,
                mCuttentTime, mCuttentTime + period);

        switch (mStatusCode) {
            case S1000:
                mUserEntityBuilder.setToken(mToken);
                break;
            case EX2013:
                mUserEntityBuilder.setStatuscode(EX2000);
                break;
            case ER5001:
                mUserEntityBuilder.setStatuscode(ER5001);
                break;
            default:
                mDBManager.release();
                mGenerator.release();
                throw new IllegalStateException("Illegal Status Code!");
        }

        mDBManager.release();
        mGenerator.release();
        return mUserEntityBuilder.build();
    }

    @Override
    public RegisterResultEntity tokenLogin(String token) {// 此方法暂未实现
        int mUserID = 0;// 暂时设置一个值
        return new RegisterResultEntity(S1000, mUserID, "");
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
