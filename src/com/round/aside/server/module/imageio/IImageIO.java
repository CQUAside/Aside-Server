package com.round.aside.server.module.imageio;

import java.io.InputStream;

import com.round.aside.server.bean.statuscode.ReadImgStatusCodeBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.IModule;

/**
 * 图片存储、读取、剪裁模块超级接口
 * 
 * @author Ghost White
 * @date 2016-4-18
 * 
 */
public interface IImageIO extends IModule {

    /**
     * 根据输入流和指定路径存储对应的图片
     * 
     * @param mInput
     *            输入图片数据流
     * @param imgPath
     *            待写入的图片路径
     * @return 结果状态码
     */
    public StatusCodeBean writeImg(InputStream mInput, String imgPath);

    /**
     * 读取指定文件路径中的图片,转化为base64编码
     * 
     * @param imgpath
     *            图片路径
     * @return 图片读取结果状态码数据bean
     */
    public ReadImgStatusCodeBean readImg(String imgpath);

    /**
     * 读取指定原图路径中的图片裁剪一个缩略图存储到指定的缩略图路径中
     * 
     * @param mOriImgPath
     *            源图路径
     * @param mClipImgPath
     *            裁切图的存储路径
     * @param mExtension
     *            文件扩展名
     * @param mMaxWidth
     *            裁切图片宽度
     * @param mMaxHeight
     *            裁切图片高度
     * @param mClipImgType
     *            裁切图保存类型
     * @return 结果状态码
     */
    public StatusCodeBean clipImg(String mOriImgPath, String mClipImgPath,
            String mExtension, int mMaxWidth, int mMaxHeight,
            String mClipImgType);

    /**
     * 对原图缩放得到一个缩略图。
     * 
     * @param mOriImgPath
     *            原图绝对路径
     * @param mThumbImgPath
     *            缩略图绝对路径
     * @param mExtension
     *            文件扩展名
     * @param mMaxWidth
     *            缩略图最大宽度
     * @param mMaxHeight
     *            缩略图最大高度
     * @param mThumbImgType
     *            缩略图的保存类型
     * @return 结果状态码
     */
    public StatusCodeBean zoomImg(String mOriImgPath, String mThumbImgPath,
            String mExtension, int mMaxWidth, int mMaxHeight,
            String mThumbImgType);

}
