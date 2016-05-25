package com.round.aside.server.module.imagepath;

import com.round.aside.server.util.MD5Utils;
import com.round.aside.server.util.StringUtil;

/**
 * 图片目录模块超级接口的实现类
 * 
 * @author Ghost_White
 * 
 */
public final class ImagePathImpl implements IImagePath {

    @Override
    public String logoImgPath(int userId, String imgId, String imgExten) {        //优化：写成一个函数
        String str = String.valueOf(userId);
        String md5path = MD5Utils.encryptionInfoByMd5(str).substring(29, 32);
        String path = "LogoImages/"+md5path+"/"+userId+"/"+imgId + (StringUtil.isEmpty(imgExten) ? "" : "." + imgExten);
        return path;
    }

    @Override
    public String originalImgPath(int userId, String imgId, String imgExten) {
        String str = String.valueOf(userId);
        String md5path = MD5Utils.encryptionInfoByMd5(str).substring(29, 32);
        String path = "OriginalImages/"+md5path+"/"+userId+"/"+imgId + (StringUtil.isEmpty(imgExten) ? "" : "." + imgExten);
        return path;
    }

    @Override
    public String thumbImgPath(int userId, String imgId, String imgExten) {
        String str = String.valueOf(userId);
        String md5path = MD5Utils.encryptionInfoByMd5(str).substring(29, 32);
        String path = "ThumbImages/"+md5path+"/"+userId+"/"+imgId + (StringUtil.isEmpty(imgExten) ? "" : "." + imgExten);
        return path;
    }

    @Override
    public String userOriginalImgDir(int userId) {
        String str = String.valueOf(userId);
        String md5path = MD5Utils.encryptionInfoByMd5(str).substring(29, 32);
        String path = "OriginalImages/"+md5path+"/"+userId;
        return path;
    }

    @Override
    public String userThumbImgDir(int userId) {
        String str = String.valueOf(userId);
        String md5path = MD5Utils.encryptionInfoByMd5(str).substring(29, 32);
        String path = "ThumbImages/"+md5path+"/"+userId;
        return path;
    }

}