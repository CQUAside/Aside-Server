package com.round.aside.server.module.accountmanager;

import java.util.LinkedList;
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

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);

        int mStatusCode = mDBManager.checkAccountExistence(mAccount);
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

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);
        int mStatusCode = mDBManager.stashRegisterPhoneAuthcode(mPhone, mAuthCode);

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

    @Override
    public RegisterResultEntity registerAccount(String mAccount, String mPassword) {
        if(StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)){
            return new RegisterResultEntity(ER5001);
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(IGenerator.class, null);
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);

        int mUserID = -1;
        int mStatusCode;

        LOOP:
            while(true){

                mUserID = mGenerator.generateUserID(0);
                mStatusCode = mDBManager.insertUser(mUserID, mAccount, mPassword);

                switch(mStatusCode){
                case S1000:
                    break LOOP;
                case F8001:
                    //userid产生了冲突，需重新生成直至插入成功为止
                    continue;
                case F8002:
                    return new RegisterResultEntity(F8003L);
                case EX2012:
                case EX2013:
                    return new RegisterResultEntity(EX2000);
                case ER5001:
                    return new RegisterResultEntity(ER5001);
                default:
                    throw new IllegalStateException("Illegal Status Code!");
                }

            }

        mGenerator.release();
        return new RegisterResultEntity(S1000, mUserID, "");
    }
    
    @Override
    public RegisterResultEntity registerAccount(String mAccount,
            String mPassword, String NickName, String Email, String PhoneNum) {

        if (StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)) {
            return new RegisterResultEntity(ER5001);
        }
        IGenerator mGenerator = ModuleObjectPool.getModuleObject(IGenerator.class, null);
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);

        int mUserID = -1;
        int mStatusCode;
        LOOP: while (true) {

            mUserID = mGenerator.generateUserID(0);
            mStatusCode = mDBManager.insertUser(mUserID, mAccount, mPassword, NickName, Email, PhoneNum);

            switch (mStatusCode) {
            case S1000:
                break LOOP;
            case F8001:
                // userid产生了冲突，需重新生成直至插入成功为止
                continue;
            case F8002:
                return new RegisterResultEntity(F8003L);
            case EX2012:
            case EX2013:
                return new RegisterResultEntity(EX2000);
            case ER5001:
                return new RegisterResultEntity(ER5001);
            default:
                throw new IllegalStateException("Illegal Status Code!");
            }

        }

        mGenerator.release();
        return new RegisterResultEntity(S1000, mUserID, "");
    }

    @Override
    public RegisterResultEntity login(String mAccount, String mPassword, long period) {
        if (StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword) || period <= 0) {
            return new RegisterResultEntity(ER5001);
        }
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);
        int mStatusCode;
        LinkedList<Integer> statusUser;
        int mUserID = -1;

        LOOP: while (true) {
            statusUser = mDBManager.selectUser(mAccount, mPassword);
            mStatusCode = statusUser.getLast();
            mUserID = statusUser.getFirst();
            switch (mStatusCode) {
            case S1000:
                break LOOP;
            case EX2012:
            case EX2016:
                return new RegisterResultEntity(EX2000);
            case ER5001:
                return new RegisterResultEntity(ER5001);
            default:
                throw new IllegalStateException("Illegal Status Code!");
            }

        }
        return new RegisterResultEntity(S1000, mUserID, "");
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
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);
        mStatus = mDBManager.selectUserStatus(mUserID);// 获取用户邮箱的状态吗，看是否以验证过；
        switch (mStatus) {
        case 0:
            System.out.print("邮箱已经验证过了");
        case S1000:
            break;
        default:
            throw new IllegalStateException("Illegal Status Code!");
        }
        String registerID = "" + Math.random() * Math.random();
        mStatusCode = mDBManager.updateUserRegister(mUserID, registerID);
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
            message.setContent("<a href= url  ></a>点击下面，完成注册</br>" + url, "text/html;charset=utf-8");
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
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);
        if (mDBManager.selectUserRegister(mUserID, VerificationCode) == S1000) {
            mDBManager.updateUserStatus(mUserID);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean findPassword(int mUserID) {
        String email = "";
        String smtp = "smtp.sina.cn";
        int mStatusCode;
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);
        if (mDBManager.selectUserStatus(mUserID) != 1) {
            System.out.print("你还没有激活邮箱");
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
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // Set Subject: header field
            message.setSubject("网站激活");

            // Now set the actual message
            message.setText("This is actual message");
            message.setContent("<h2>下面是你的验证码</h2></br>" + registerID, "text/html;charset=utf-8");
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
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);
        if (mDBManager.selectUserRegister(mUserID, verificationCode) == S1000) {
            mDBManager.updateUserStatus(mUserID);
            return true;
        } else {
            return false;
        }
    }
}
