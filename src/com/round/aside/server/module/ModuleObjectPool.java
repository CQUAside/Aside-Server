package com.round.aside.server.module;

import java.util.HashMap;
import java.util.Map;

import com.round.aside.server.module.accountmanager.AccountManagerModuleFactoryImpl;
import com.round.aside.server.module.accountmanager.IAccountManager;

public final class ModuleObjectPool {
	
	private static final Map<String, IModuleFactory<? extends IModule>> mModuleFactoryMap = 
			new HashMap<String, IModuleFactory<? extends IModule>>();
	
	static{
		mModuleFactoryMap.put(IAccountManager.class.getSimpleName(), new AccountManagerModuleFactoryImpl());
	}
	
	private ModuleObjectPool(){}
	
	public static <T extends IModule> T getModuleObject(Class<?> mModuleName, Object o1, Object ... objects){
		@SuppressWarnings("unchecked")
		IModuleFactory<T> mModuleFactory = (IModuleFactory<T>)mModuleFactoryMap.get(mModuleName.getSimpleName());
		return mModuleFactory.createModule(o1, objects);
	}
	

}
