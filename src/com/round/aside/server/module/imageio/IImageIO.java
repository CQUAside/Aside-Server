package com.round.aside.server.module.imageio;

import java.io.InputStream;

import com.round.aside.server.module.IModule;
import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgClipResultEnum;
import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgRWResultEnum;
import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgZoomResultEnum;

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
     * @return 图片存取结果枚举
     */
    public ImgRWResultEnum writeImg(InputStream mInput, String imgPath);

    /**
     * 读取指定文件路径中的图片,转化为base64编码
     * 
     * @param imgpath
     *            图片路径
     * @return 图片读取结果实体
     */
    public ReadImageResultEntity readImg(String imgpath);

    /**
     * 读取指定原图路径中的图片裁剪一个缩略图存储到指定的缩略图路径中
     * 
     * @param mOriImgPath
     *            源图路径
     * @param mThumbImgPath
     *            缩略图存储路径
     * @param mMaxWidth
     *            剪切后图片宽度
     * @param mMaxHeight
     *            剪切后图片高度
     * @return
     */
    public ImgClipResultEnum clipImg(String mOriImgPath, String mThumbImgPath,
            int mMaxWidth, int mMaxHeight);

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
     * @return 缩放结果
     */
    public ImgZoomResultEnum zoomImg(String mOriImgPath, String mThumbImgPath,
            String mExtension, int mMaxWidth, int mMaxHeight);

}
