package com.round.aside.server.module.personalAdsmanager;

import com.round.aside.server.module.IModuleFactory;



/**
 * 
 * 用户个人广告管理模块的工厂实现类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 */
public class PersonalAdsManagerModuleFactoryImpl implements IModuleFactory<IPersonalAdsManager>{

    @Override
    public IPersonalAdsManager createModule(Object o1, Object... objects) {
        return new PersonalAdsManagerImpl();
    }

}
