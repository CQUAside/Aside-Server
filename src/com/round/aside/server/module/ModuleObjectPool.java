package com.round.aside.server.module;

import java.util.HashMap;
import java.util.Map;

import com.round.aside.server.module.accountmanager.AccountManagerModuleFactoryImpl;
import com.round.aside.server.module.accountmanager.IAccountManager;

/**
 * 模块对象池，用于对外暴露接口供第三方使用获取模块对象，隐藏了模块实现和工厂实现。
 * 
 * @author A Shuai
 *
 */
public final class ModuleObjectPool {

    private static final Map<String, IModuleFactory<? extends IModule>> mModuleFactoryMap = 
            new HashMap<String, IModuleFactory<? extends IModule>>();

    static {
        mModuleFactoryMap.put(IAccountManager.class.getSimpleName(), new AccountManagerModuleFactoryImpl());
    }

    private ModuleObjectPool() {
    }

    public static <T extends IModule> T getModuleObject(Class<?> mModuleName, Object o1, Object... objects) {
        @SuppressWarnings("unchecked")
        IModuleFactory<T> mModuleFactory = (IModuleFactory<T>) mModuleFactoryMap.get(mModuleName.getSimpleName());
        return mModuleFactory.createModule(o1, objects);
    }

}
