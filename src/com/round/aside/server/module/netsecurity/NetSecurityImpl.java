package com.round.aside.server.module.netsecurity;

import static com.round.aside.server.constant.StatusCode.*;

import com.round.aside.server.bean.statuscode.StatusCodeBean;

/**
 * 网络安全模块实现类
 * 
 * @author A Shuai
 * @date 2015-12-28
 * 
 */
public final class NetSecurityImpl implements INetSecurity {

    @Override
    public StatusCodeBean checkTokenLegal(int mUserID, String mToken) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();
        mBuilder.setStatusCode(S1000).setMsg("验证通过");
        return mBuilder.build();
    }

}
