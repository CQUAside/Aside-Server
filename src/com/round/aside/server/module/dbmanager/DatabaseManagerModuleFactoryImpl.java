package com.round.aside.server.module.dbmanager;

import com.round.aside.server.module.IModuleFactory;

/**
 * 数据库管理模块工厂实现类
 * 
 * @author A Shuai
 * @date 2016-3-28
 *
 */
public final class DatabaseManagerModuleFactoryImpl implements IModuleFactory<IDatabaseManager>{

    @Override
    public IDatabaseManager createModule(Object o1, Object... objects) {
        return new DatabaseManagerImpl();
    }

}
