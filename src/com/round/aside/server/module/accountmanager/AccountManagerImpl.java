package com.round.aside.server.module.accountmanager;

import com.round.aside.server.entity.RegisterResultEntity;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;
import com.round.aside.server.module.generator.IGenerator;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.*;

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
        if(StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)){
            return new RegisterResultEntity(ER5001);
        }

        IGenerator mGenerator = ModuleObjectPool.getModuleObject(IGenerator.class, null);
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(IDatabaseManager.class, null);

        int mUserID = -1;
        int mStatusCode;

        LOOP:
            while(true){

                mUserID = mGenerator.generateUserID(1000);
                mStatusCode = mDBManager.insertUser(mUserID, mAccount, mPassword);

                switch(mStatusCode){
                case S1000:
                    break LOOP;
                case F8001:
                    //userid产生了冲突，需重新生成直至插入成功为止
                    continue;
                case F8002:
                    return new RegisterResultEntity(F8003L);
                case EX2012:
                case EX2013:
                    return new RegisterResultEntity(EX2000);
                case ER5001:
                    return new RegisterResultEntity(ER5001);
                default:
                    throw new IllegalStateException("Illegal Status Code!");
                }

            }

        return new RegisterResultEntity(S1000, mUserID, "");
    }

}
