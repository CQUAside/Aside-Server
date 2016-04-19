package com.round.aside.server.module.imageio;

import java.io.InputStream;

import com.round.aside.server.module.IModule;

/**
 * 图片存储、读取、剪裁模块超级接口
 * @author Ghost White
 * @date 2016-4-18
 *
 */
public interface IImageIO extends IModule {
    
    public ImageIOResultEnum writeImg(InputStream mInput,String imagePath);
    
    

}
