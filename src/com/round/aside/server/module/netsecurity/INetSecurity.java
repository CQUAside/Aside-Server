package com.round.aside.server.module.netsecurity;

import com.round.aside.server.module.IModule;

/**
 * 网络安全模块超级接口
 * 
 * @author A Shuai
 * @date 2015-12-28
 * 
 */
public interface INetSecurity extends IModule {

    /**
     * 检查此次Token的网络安全性
     * 
     * @param mUserID
     * @param mToken
     * @return 结果状态码，分别为{@link #S1000}安全合法
     */
    int checkTokenLegal(int mUserID, String mToken);
    
}
