package com.round.aside.server.module.accountmanager;

import com.round.aside.server.util.StringUtil;

public class AccountManagerImpl implements IAccountManager{

	@Override
	public boolean isExistAccount(String mAccount) {
		if(StringUtil.isEmpty(mAccount)){
			return false;
		}
		return true;
	}

	
	
}
