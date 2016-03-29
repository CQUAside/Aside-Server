package com.round.aside.server.module.generator;

import com.round.aside.server.module.IModuleFactory;

/**
 * ID生成器模块工厂实现类
 * 
 * @author A Shuai
 * @date 2016-3-28
 *
 */
public final class GeneratorModuleFactoryImpl implements IModuleFactory<IGenerator>{

    @Override
    public IGenerator createModule(Object o1, Object... objects) {
        return new GeneratorImpl();
    }

}
