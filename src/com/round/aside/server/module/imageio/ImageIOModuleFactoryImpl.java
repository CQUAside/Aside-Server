package com.round.aside.server.module.imageio;

import com.round.aside.server.module.IModuleFactory;

public final class ImageIOModuleFactoryImpl implements IModuleFactory<IImageIO> {

    @Override
    public IImageIO createModule(Object o1, Object... objects) {  
        return new ImageIOImpl();
    }

}
