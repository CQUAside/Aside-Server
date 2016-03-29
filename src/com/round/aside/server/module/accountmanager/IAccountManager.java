package com.round.aside.server.module.accountmanager;

import com.round.aside.server.constant.StatusCode;
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
     *            账号
     * @param mPassword
     *            密码
     * @return 此次注册操作的结果，其中包含了各种情况下对应的状态码，有且只有四中结果码，分别为
     * {@link StatusCode#S1000}成功码， {@link StatusCode#EX2011}Connection获取失败异常， {@link StatusCode#EX2012}SQL插入异常，
     * {@link StatusCode#EX2013}SQL更新异常。
     */
    RegisterResultEntity registerAccount(String mAccount, String mPassword);

}
