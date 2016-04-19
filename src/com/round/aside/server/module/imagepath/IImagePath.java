package com.round.aside.server.module.imagepath;

import com.round.aside.server.module.IModule;

/**
 * 图片目录模块超级接口
 * 
 * @author Ghost_White
 * 
 */
public interface IImagePath extends IModule {
    
    /**
     * 获取Logo图文件路径
     * @param userId
     *          用户id
     * @param imgId
     *          图片id
     * @return
     */
    String logoImgPath(int userId,String imgId);
    
    
    /**
     * 获取原图文件路径
     * @param userId
     *          用户id
     * @param imgId
     *          图片id
     * @return
     */
    String originalImgPath(int userId,String imgId);
    
    /**
     * 获取缩略图文件路径
     * @param userId
     *          用户id
     * @param imgId
     *          图片id
     * @return
     */
    String thumbImgPath(int userId,String imgId);
    
    /**
     * 获取一个用户对应的原图目录
     * @param userId
     *          用户id
     * @return
     */
    String userOriginalImgDir(int userId);
    
    /**
     * 获取一个用户对应的缩略图目录
     * @param uerId
     *          用户id
     * @return
     */
    String userThumbImgDir(int userId);
    
}
