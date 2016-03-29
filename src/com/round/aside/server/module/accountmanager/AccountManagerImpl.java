package com.round.aside.server.module.accountmanager;

import com.round.aside.server.constant.StatusCode;
import com.round.aside.server.entity.RegisterResultEntity;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;
import com.round.aside.server.module.generator.IGenerator;
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

    @Override
    public RegisterResultEntity registerAccount(String mAccount, String mPassword) {

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(IGenerator.class, null);
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);

        int mUserID = -1;
        int mStatusCode;

        LOOP:
            while(true){

                mUserID = mGenerator.generateUserID(1000);
                mStatusCode = mDBManager.insertUserID(mUserID);

                switch(mStatusCode){
                case StatusCode.S1000:
                    break LOOP;
                case StatusCode.F8001:
                    //这次生成了同值的主键，需重新生成直至插入成功为止
                    continue;
                case StatusCode.EX2011:
                case StatusCode.EX2012:
                    return new RegisterResultEntity(mStatusCode);
                default:
                    throw new IllegalStateException("Illegal Status Code!");
                }

            }

        mStatusCode = mDBManager.updateUser(mUserID, mAccount, mPassword);
        switch(mStatusCode){
        case StatusCode.S1000:
            break;
        case StatusCode.EX2011:
        case StatusCode.EX2012:
            return new RegisterResultEntity(mStatusCode);
        }

        return new RegisterResultEntity(StatusCode.S1000, mUserID, "");
    }

}
