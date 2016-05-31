package com.round.aside.server.util;

import static com.round.aside.server.constant.Constants.EMAIL_SMTP_SERVER;
import static com.round.aside.server.constant.Constants.SEND_EMAIL_ADDRESS;
import static com.round.aside.server.constant.Constants.SEND_EMAIL_PASSWORD;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.round.aside.server.bean.EmailContentBean;

/**
 * 辅助工具类。包括发送短信，发送邮件。
 * 
 * @author A Shuai
 * @date 2016-4-30
 * 
 */
public final class AssistUtils {

    private AssistUtils() {
    }

    /**
     * 根据指定手机号发送一个随机生成的四位长度的验证码
     * 
     * @param phone
     *            待发送验证码的手机号
     * @return 如果执行成功，则返回对应验证码；若执行失败，则返回空字符串
     */
    public static String sendPhoneAuth(String phone) {
        return "1111";
    }

    /**
     * 向指定的Email地址发送一封邮箱认证邮件
     * 
     * @param mEmail
     *            目标Email地址
     * @param mAuthCode
     *            认证码
     * @return true为发送成功，否则为发送失败
     */
    public static boolean sendActivationEmailAuthCode(String mEmail,
            String mAuthCode) {

        EmailContentBean.Builder mEmailBuilder = new EmailContentBean.Builder();
        mEmailBuilder.setSubject("邮箱激活");
        mEmailBuilder.setText("This is actual message");
        mEmailBuilder.setContent("<a href= url  ></a>点击下面，完成注册</br>"
                + " ?authcode=" + mAuthCode);
        mEmailBuilder.setContentType("text/html;charset=utf-8");

        return sendEmail(mEmail, mEmailBuilder.build());
    }

    /**
     * 向指定的Email地址发送一封密码找回邮件
     * 
     * @param mEmail
     *            目标Email地址
     * @param mAuthCode
     *            认证码
     * @return true为发送成功，否则为发送失败
     */
    public static boolean sendRetrieverPasswordAuthCode(String mEmail,
            String mAuthCode) {

        EmailContentBean.Builder mEmailBuilder = new EmailContentBean.Builder();
        mEmailBuilder.setSubject("密码找回");
        mEmailBuilder.setText("This is actual message");
        mEmailBuilder.setContent("<h2>下面是你的验证码</h2></br>" + " ?authcode="
                + mAuthCode);
        mEmailBuilder.setContentType("text/html;charset=utf-8");

        return sendEmail(mEmail, mEmailBuilder.build());
    }

    /**
     * 向指定邮件地址发送指定邮件的静态方法
     * 
     * @param mEmail
     *            邮件目的地址
     * @param mEmailContentBean
     *            邮件内容数据bean
     * @return true为发送成功，否则为发送失败
     */
    private static boolean sendEmail(String mEmail,
            EmailContentBean mEmailContentBean) {
        // 获得系统属性
        Properties properties = System.getProperties();
        // 设置邮件主机
        properties.setProperty("mail.smtp.host", EMAIL_SMTP_SERVER);
        properties.setProperty("mail.smtp.auth", "true");
        // 设置我方网站地址，用户在点击后，返回本站
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(SEND_EMAIL_ADDRESS));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    mEmail));
            // Set Subject: header field
            message.setSubject(mEmailContentBean.getSubject());
            // Now set the actual message
            message.setText(mEmailContentBean.getText());
            message.setContent(mEmailContentBean.getContent(),
                    mEmailContentBean.getContentType());
            // Send message
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(EMAIL_SMTP_SERVER, SEND_EMAIL_ADDRESS,
                    SEND_EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

}
