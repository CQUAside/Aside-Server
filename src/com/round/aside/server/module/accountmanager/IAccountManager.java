package com.round.aside.server.module.accountmanager;

import com.round.aside.server.module.IModule;

public interface IAccountManager extends IModule{

	/**
	 * 用户注册时查询指定账号是否已经合法，包括查验字符串的合法性，数据库中是否已存在相同账号等
	 * 
	 * @param mAccount，待查询账号
	 * @return true为合法
	 */
	boolean isExistAccount(String mAccount);
	
}
