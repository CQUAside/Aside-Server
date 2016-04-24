package com.round.aside.server.module.accountmanager;

import com.round.aside.server.entity.RegisterResultEntity;
import com.round.aside.server.module.IModule;

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
     * @return true为合法
     */
    boolean isLegalRegisteredAccount(String mAccount);

    /**
     * 两参数的注册账号接口
     * 
     * @param mAccount
     *            账号，不能为空
     * @param mPassword
     *            密码，不能为空
     * @return 此次注册操作的结果，其中包含了各种情况下对应的状态
     */
    RegisterResultEntity registerAccount(String mAccount, String mPassword);
    
    /**
     * 
     * @param mAccount
     * 			账号，不能为空
     * @param mPassword
     * 		             密码，不能为空
     * @param NickName
     *          昵称，可为空
     * @param Email
     *          邮箱，可为空
     * @param PhoneNum
     *          电话，可为空
     * @return 此次注册操作的结果，其中包含了各种情况下对应的状态
     */
    RegisterResultEntity registerAccount(String mAccount, String mPassword,String NickName,String Email,String PhoneNum);
    
    /**
     * 
     * @param mAccount
     *        账号，不能为空
     * @param mPassword
     *        密码，不能为空
     * @param period
     *        时间
     * @return 此次注册操作的结果，其中包含了各种情况下对应的状态
     */
    RegisterResultEntity login(String mAccount,String mPassword,long period);
    
    /**
     * 
     * @param token
     * @return 此次注册操作的结果，其中包含了各种情况下对应的状态
     */
    RegisterResultEntity tokenLogin(String token);
    
    /**
     * 
     * @param mUserID
     *        用户ID
     * @param email
     *        接受方的Email
     * @return true表示发送验证邮箱，否则发送失败
     */       
    boolean activationEmail(int mUserID,String email);
    
    /**
     * 
     * @param mUserID
     *         用户ID
     * @param VerificationCode
     *         验证码
     * @return true表示激活邮箱，否则激活失败
     */
    boolean validationctivationEmail(int mUserID,String VerificationCode);

    /**
     * 
     * @param mUserID
     *        用户ID
     * @return 表示发送找回密码邮件，否则发送失败
     */
    boolean findPassword(int mUserID);
    
    /**
     * 
     * @param mUserID
     *        用户ID
     * @param verificationCode
     *        验证码
     * @return 返回true说明验证成功，否则失败
     */
    boolean validationFindPassword(int mUserID,String verificationCode);
}
