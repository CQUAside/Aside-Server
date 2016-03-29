package com.round.aside.server.module.dbmanager;

import com.round.aside.server.constant.StatusCode;
import com.round.aside.server.module.IModule;

/**
 * 数据库管理模块超级接口
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public interface IDatabaseManager extends IModule {

    /**
     * 插入用户ID
     * 
     * @param mUserID
     *            待插入的用户ID
     * @return 操作结果状态值，结果值只有四种，分别为{@link StatusCode#S1000}成功，
     *         {@link StatusCode#F8001} 主键冲突， {@link StatusCode#EX2011}
     *         Connection获取失败异常， {@link StatusCode#EX2012}SQL插入异常。
     */
    int insertUserID(int mUserID);

    /**
     * 更新用户信息
     * 
     * @param mUserID
     *            待更新数据的用户对应的ID
     * @param mAccount
     *            账号
     * @param mPassword
     *            密码
     * @return 操作结果状态值，结果值只有三种，分别为{@link StatusCode#S1000}成功，
     *         {@link StatusCode#EX2011}Connection获取失败异常，
     *         {@link StatusCode#EX2013}SQL更新异常。
     */
    int updateUser(int mUserID, String mAccount, String mPassword);
    

}
