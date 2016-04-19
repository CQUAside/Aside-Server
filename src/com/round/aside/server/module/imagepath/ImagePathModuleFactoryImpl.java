package com.round.aside.server.module.imagepath;

import com.round.aside.server.module.IModuleFactory;

/**
 * 图片路径模块工厂实现类
 * 
 * @author Ghost_White
 * 
 */
public final class ImagePathModuleFactoryImpl implements IModuleFactory<IImagePath> {

    @Override
    public IImagePath createModule(Object o1, Object... objects) {
        return new ImagePathImpl();
    }

}
