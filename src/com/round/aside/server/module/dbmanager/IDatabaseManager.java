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
     * 插入一条用户信息
     * 
     * @param mAccount
     * @param mPassword
     * @return
     */
    int insertUser(String mAccount, String mPassword);
    
}
