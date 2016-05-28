package com.round.aside.server.module.accountmanager;

import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.statuscode.LoginUserBean;
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
     *         {@link #R6001}用户名命名非法，{@link #ER5001}参数为空，{@link #EX2000}数据库异常，可重试。
     */
    StatusCodeBean checkRegisteredAccountLegal(String mAccount);

    /**
     * 发送账号注册时的手机验证码
     * 
     * @param mPhone
     *            手机号码
     * @return 结果状态码
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
     * @return 此次注册操作的结果，其中包含了各种情况下对应的状态，分别为
     */
    LoginUserBean registerAccount(String mAccount, String mPassword,
            String mPhone, String mAuthcode, RequestInfoBean mRequestInfoBean);

    /**
     * 登陆
     * 
     * @param mAccount
     *            账号，不能为空
     * @param mPassword
     *            密码，不能为空
     * @param period
     *            申请Token的有效期
     * @param mRequestInfoBean
     *            登陆请求方的相关信息
     * @return 此次登陆操作的结果，其中包含了各种情况下对应的状态
     */
    LoginUserBean login(String mAccount, String mPassword, long period,
            RequestInfoBean mRequestInfoBean);

    /**
     * Token登陆
     * 
     * @param userId
     *            用户ID
     * @param token
     *            Token令牌
     * @return 此次注册操作的结果状态码，有且仅有以下几种，分别为{@link #S1000}合法，{@link #ER5001}调用参数非法，
     *         {@link #R6006}Token非法，{@link #R6007}Token失效，{@link #EX2016}SQL查询执行异常。
     */
    StatusCodeBean verifyToken(int userId, String token);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @param email
     *            接受方的Email
     * @return true表示发送验证邮箱，否则发送失败
     */
    boolean activationEmail(int mUserID, String email);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @param VerificationCode
     *            验证码
     * @return true表示激活邮箱，否则激活失败
     */
    boolean validationctivationEmail(int mUserID, String VerificationCode);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @return 表示发送找回密码邮件，否则发送失败
     */
    boolean findPassword(int mUserID);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @param verificationCode
     *            验证码
     * @return 返回true说明验证成功，否则失败
     */
    boolean validationFindPassword(int mUserID, String verificationCode);
}
