package com.round.aside.server.module.accountmanager;

import com.round.aside.server.util.StringUtil;

/**
 * 账号管理模块超级接口的实现类
 * 
 * @author A Shuai
 * 
 */
public class AccountManagerImpl implements IAccountManager {

    @Override
    public boolean isExistAccount(String mAccount) {
        if (StringUtil.isEmpty(mAccount)) {
            return false;
        }
        return true;
    }

}
