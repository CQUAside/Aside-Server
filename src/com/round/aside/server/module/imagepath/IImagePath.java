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
     * 
     * @param userId
     *            用户id
     * @param imgId
     *            图片id
     * @param imgExten
     *            图片文件扩展名，可以为空
     * @return Logo图文件路径
     */
    String getLogoImgPath(int userId, String imgId, String imgExten);

    /**
     * 获取原图文件路径
     * 
     * @param userId
     *            用户id
     * @param imgId
     *            图片id
     * @param imgExten
     *            图片文件扩展名，可以为空
     * @return 原图文件路径
     */
    String getOriginalImgPath(int userId, String imgId, String imgExten);

    /**
     * 获取缩略图文件路径
     * 
     * @param userId
     *            用户id
     * @param imgId
     *            图片id
     * @param imgExten
     *            图片文件扩展名，可以为空
     * @return 缩略图文件路径
     */
    String getThumbImgPath(int userId, String imgId, String imgExten);

    /**
     * 获取一个用户对应的原图目录
     * 
     * @param userId
     *            用户id
     * @return 用户原图片集所在的目录
     */
    String getUserOriginalImgDir(int userId);

    /**
     * 获取一个用户对应的缩略图目录
     * 
     * @param uerId
     *            用户id
     * @return 用户缩略图集所在的目录
     */
    String getUserThumbImgDir(int userId);

}
