package com.round.aside.server.module.imagepath;

import java.io.File;

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
    public String getLogoImgPath(int userId, String imgId, String imgExten) {
        return buildImgPath("LogoImg", userId, imgId, imgExten);
    }

    @Override
    public String getOriginalImgPath(int userId, String imgId, String imgExten) {
        return buildImgPath("OriginalImg", userId, imgId, imgExten);
    }

    @Override
    public String getThumbImgPath(int userId, String imgId, String imgExten) {
        return buildImgPath("ThumbImg", userId, imgId, imgExten);
    }

    private final String buildImgPath(String mImageRootDir, int mUserID,
            String mImgID, String mImgExten) {
        StringBuilder mSB = new StringBuilder(70);
        mSB.append(mImageRootDir).append(File.separatorChar);
        mSB.append(MD5Utils.encryptionInfoByMd5(mUserID + "").substring(29, 32));
        mSB.append(File.separatorChar).append(mUserID).append(File.separatorChar).append(mImgID);
        if (!StringUtil.isEmpty(mImgExten)) {
            mSB.append('.').append(mImgExten);
        }
        return mSB.toString();
    }

    @Override
    public String getUserOriginalImgDir(int userId) {
        return buildImgDir("OriginalImg", userId);
    }

    @Override
    public String getUserThumbImgDir(int userId) {
        return buildImgDir("ThumbImg", userId);
    }

    private final String buildImgDir(String mImageRootDir, int mUserID) {
        StringBuilder mSB = new StringBuilder(30);
        mSB.append(mImageRootDir).append(File.separatorChar);
        mSB.append(MD5Utils.encryptionInfoByMd5(mUserID + "").substring(29, 32));
        mSB.append(File.separatorChar).append(mUserID);
        return mSB.toString();
    }

}