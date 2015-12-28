package com.round.aside.server.module.accountmanager;

import com.round.aside.server.module.IModuleFactory;

/**
 * 账号管理模块工厂实现类
 * 
 * @author A Shuai
 * 
 */
public class AccountManagerModuleFactoryImpl implements IModuleFactory<IAccountManager> {

    @Override
    public IAccountManager createModule(Object o1, Object... objects) {
        return new AccountManagerImpl();
    }

}
