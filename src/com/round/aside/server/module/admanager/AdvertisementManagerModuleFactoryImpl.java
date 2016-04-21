package com.round.aside.server.module.admanager;

import com.round.aside.server.module.IModuleFactory;

/**
 * 广告管理工厂实现类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 *
 */
public class AdvertisementManagerModuleFactoryImpl implements IModuleFactory<IAdvertisementManager>{

    @Override
    public IAdvertisementManager createModule(Object o1, Object... objects) {
        return new AdvertisementManagerImpl();
    }

}
