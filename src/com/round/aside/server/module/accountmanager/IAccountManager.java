package com.round.aside.server.module.accountmanager;

import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.IModule;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 账号管理模块超级接口
 * 
 * @author A Shuai
 * 
 */
public interface IAccountManager extends IModule {

    /**
     * 用户注册时查询指定账号是否已经合法，包括查验字符串的合法性，数据库中是否已存在相同账号等
     * 
     * @param mAccount
     *            待查询账号
     * @return 结果状态码，合法的结果值有几种，分别为{@link #S1000}完全合法，{@link #R6002}账号重复，
     *         {@link #R6001}用户名命名非法，{@link #ER5001}参数为空，{@link #EX2010}
     *         数据库异常，可重试。
     */
    StatusCodeBean checkRegisteredAccountLegal(String mAccount);

    /**
     * 发送账号注册时的手机验证码
     * 
     * @param mPhone
     *            手机号码
     * @return 结果状态码，分别为{@link #S1000}手机认证码发送成功，{@link #ER5001}手机号参数错误，
     *         {@link #ER5002}手机号非法，{@link #R6003}认证码发送失败，{@link #EX2010}
     *         数据库操作异常，请重试。
     */
    StatusCodeBean sendPhoneAuthcode(String mPhone);

    /**
     * 注册账号接口
     * 
     * @param mAccount
     *            账号，不能为空
     * @param mPassword
     *            密码，不能为空
     * @param mPhone
     *            手机号
     * @param mAuthcode
     *            手机认证码，为四位随机数字字符串
     * @param mRequestInfoBean
     *            请求方法的相关信息
     * @return 注册结果包括结果状态码，其中包含了各种情况下对应的状态，分别为{@link #S1000}注册成功；{@link #ER5001}
     *         参数非法；{@link #ER5002}手机号非法；{@link #EX2010}数据库操作异常，请重试；
     *         {@link #ER5003L}手机号与认证码不符；{@link #ER5004L}手机认证码超时；{@link #F8003L}
     *         账号重复。
     */
    UserIDTokenSCBean registerAccount(String mAccount, String mPassword,
            String mPhone, String mAuthcode, RequestInfoBean mRequestInfoBean);

    /**
     * 普通登陆
     * 
     * @param mAccount
     *            账号，不能为空
     * @param mPassword
     *            密码，不能为空
     * @param period
     *            申请Token的有效期
     * @param mRequestInfoBean
     *            登陆请求方的相关信息
     * @return 登陆结果包括结果状态码，其中包含了各种情况下对应的状态{@link #S1000}登陆成功；{@link #ER5001}
     *         参数非法；{@link #R6004}账号不存在；{@link #R6005}密码错误；{@link #EX2010}
     *         数据库操作异常，请重试。
     */
    UserIDTokenSCBean login(String mAccount, String mPassword, long period,
            RequestInfoBean mRequestInfoBean);

    /**
     * Token登陆
     * 
     * @param userId
     *            用户ID
     * @param token
     *            Token令牌
     * @return 此次注册操作的结果状态码，有且仅有以下几种，分别为{@link #S1000}合法；{@link #ER5001}调用参数非法；
     *         {@link #R6006}Token非法；{@link #R6007}Token已失效；{@link #EX2010}
     *         数据库操作异常，请重试。
     */
    StatusCodeBean verifyToken(int userId, String token);

    /**
     * 根据提供的用户ID，检查对应的用户是否为管理员权限。<br>
     * 用于执行一些管理员权限操作时进行检查
     * 
     * @param mUserID
     *            带检查权限的用户ID
     * @return 结果状态码，有且仅有一下几种，分别为{@link #S1000}确认为管理员权限；{@link #ER5001}调用参数非法；
     *         {@link #EX2010}数据库操作异常，请重试。
     */
    StatusCodeBean verifyAdminPermission(int mUserID);

    /**
     * 向指定UserID对应的用户注册邮箱发送邮箱验证邮件
     * 
     * @param mUserID
     *            用户ID
     * @return 结果状态数据bean。状态值有且仅有以下几种，分别为{@link #S1000}验证邮件发送成功；{@link #R6013}
     *         邮箱已经验证通过，无需再次认证；{@link #ER5001}UserID参数非法；{@link #R6008}
     *         无此UserID用户；{@link #EX2010}数据库操作异常，请重试；{@link #R6009}邮件发送失败。
     */
    StatusCodeBean activateEmail(int mUserID);

    /**
     * 验证邮箱认证码
     * 
     * @param mUserID
     *            待认证方的用户ID
     * @param mEmail
     *            待认证方邮箱地址
     * @param mAuthCode
     *            验证码
     * @return 结果状态数据bean。状态值有且仅有以下几种，分别为{@link #S1000}邮箱验证通过；{@link #ER5001}
     *         参数非法；{@link #R6008}无此UserID和Email对应的认证请求；{@link #EX2010}
     *         数据库操作异常，请重试；{@link #R6010}认证码不符，认证失败。
     */
    StatusCodeBean validateActivationEmail(int mUserID, String mEmail,
            String mAuthCode);

    /**
     * 找回密码
     * 
     * @param mUserID
     *            用户ID
     * @return 结果状态数据bean。状态值有且仅有以下几种，分别为{@link #S1000}密码找回邮件发送成功；{@link #R6009}
     *         密码找回邮件发送失败；{@link #R6011}邮箱认证失败，不可找回密码；{@link #R6012}
     *         邮箱认证失败，不可找回密码；{@link #ER5001}UserID参数非法；{@link #R6008}
     *         无此UserID对应的用户；{@link #EX2010}数据库操作异常，请重试。
     */
    StatusCodeBean retrievePassword(int mUserID);

    /**
     * 验证找回密码认证码
     * 
     * @param mUserID
     *            用户ID
     * @param mEmail
     *            待认证方邮箱地址
     * @param mAuthCode
     *            验证码
     * @return 结果状态数据bean。状态值有且仅有以下几种，分别为{@link #S1000}认证成功，准许修改密码；
     *         {@link #R6010}认证失败，不许修改密码，请重新申请；{@link #R6008}
     *         无此UserID和Email的找回密码请求；{@link #ER5001}参数非法；{@link #EX2010}
     *         数据库操作异常，请重试。
     */
    StatusCodeBean validationRetrieverPassword(int mUserID, String mEmail,
            String mAuthCode);
}
