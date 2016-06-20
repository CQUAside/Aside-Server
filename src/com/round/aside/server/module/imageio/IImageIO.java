package com.round.aside.server.module.imageio;

import java.io.InputStream;

import com.round.aside.server.bean.statuscode.ReadImgStatusCodeBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.IModule;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 图片存储、读取、剪裁模块超级接口<br>
 * 本模块中的图片路径形参均为URL相对地址，在本模块的具体存储中应自行包装。
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
     * @param imgRelativePath
     *            待写入的图片相对Url路径
     * @return 结果状态码，合法的结果值分别有：{@link #S1000}成功；{@link #ER5001}形参非法；
     *         {@link #R6015}目的图片文件目录创建失败；{@link #EX2031}目的图片文件异常；
     *         {@link #EX2032}文件IO异常。
     */
    public StatusCodeBean writeImg(InputStream mInput, String imgRelativePath);

    /**
     * 读取指定文件路径中的图片,转化为base64编码
     * 
     * @param imgRelativePath
     *            图片相对Url路径
     * @return 图片读取结果状态码数据bean，合法的状态码分别有：{@link #S1000}成功；{@link #ER5001}图片路径为空；
     *         {@link #EX2031}源图片不存在；{@link #EX2032}原图读取异常。
     */
    public ReadImgStatusCodeBean readImg(String imgRelativePath);

    /**
     * 读取指定原图路径中的图片裁剪一个缩略图存储到指定的缩略图路径中
     * 
     * @param mOriImgRelaPath
     *            源图相对Url路径
     * @param mClipImgRelaPath
     *            裁切图的存储相对Url路径
     * @param mExtension
     *            文件扩展名
     * @param mMaxWidth
     *            裁切图片宽度
     * @param mMaxHeight
     *            裁切图片高度
     * @param mClipImgType
     *            裁切图保存类型
     * @return 结果状态码，合法的状态码分别有：{@link #S1000}成功；{@link #ER5001}形参非法；
     *         {@link #R6014}源图不存在或读取失败；{@link #R6015}裁剪图目录创建失败； {@link #EX2032}
     *         裁剪图写入异常。
     */
    public StatusCodeBean clipImg(String mOriImgRelaPath, String mClipImgRelaPath,
            String mExtension, int mMaxWidth, int mMaxHeight,
            String mClipImgType);

    /**
     * 对原图缩放得到一个缩略图。
     * 
     * @param mOriImgRelaPath
     *            原图相对Url路径
     * @param mThumbImgRelaPath
     *            缩略图相对Url路径
     * @param mExtension
     *            文件扩展名
     * @param mMaxWidth
     *            缩略图最大宽度
     * @param mMaxHeight
     *            缩略图最大高度
     * @param mThumbImgType
     *            缩略图的保存类型
     * @return 结果状态码，合法的状态码分别有：{@link #S1000}成功；{@link #ER5001}形参非法；
     *         {@link #R6014}源图不存在或读取失败；{@link #R6015}缩略图目录创建失败； {@link #EX2032}
     *         缩略图写入异常。
     */
    public StatusCodeBean zoomImg(String mOriImgRelaPath, String mThumbImgRelaPath,
            String mExtension, int mMaxWidth, int mMaxHeight,
            String mThumbImgType);

}
