package com.round.aside.server.module.dbmanager;

import com.round.aside.server.module.IModule;

/**
 * 数据库管理模块超级接口
 * 
 * @author A Shuai
 * @date 2016-3-28
 *
 */
public interface IDatabaseManager extends IModule{

    /**
     * 插入用户ID
     * 
     * @param mUserID 待插入的用户ID
     * @return 
     */
    int insertUserID(int mUserID);
    
    /**
     * 更新用户信息
     * 
     * @param mUserID
     * @param mAccount
     * @param mPassword
     * @return
     */
    int updateUser(int mUserID, String mAccount, String mPassword);
    
}
