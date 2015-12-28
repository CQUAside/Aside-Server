package com.round.aside.server.module.netsecurity;

import com.round.aside.server.module.IModuleFactory;

/**
 * 网络安全模块的工厂实现类
 * 
 * @author A Shuai
 * @date 2015-12-28
 *
 */
public final class NetSecurityModuleFactoryImpl implements IModuleFactory<INetSecurity>{

    @Override
    public INetSecurity createModule(Object o1, Object... objects) {
        return new NetSecurityImpl();
    }

}
