package com.round.aside.server.module.netsecurity;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 网络安全模块实现类
 * 
 * @author A Shuai
 * @date 2015-12-28
 * 
 */
public final class NetSecurityImpl implements INetSecurity {

    @Override
    public int checkTokenLegal(int mUserID, String mToken) {
        return S1000;
    }

}
