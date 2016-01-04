package com.round.aside.server.module.accountmanager;

import com.round.aside.server.util.StringUtil;

/**
 * 账号管理模块超级接口的实现类
 * 
 * @author A Shuai
 * 
 */
public final class AccountManagerImpl implements IAccountManager {

    @Override
    public boolean isLegalRegisteredAccount(String mAccount) {
        if (StringUtil.isEmpty(mAccount)) {
            return false;
        }
        return true;
    }

}
