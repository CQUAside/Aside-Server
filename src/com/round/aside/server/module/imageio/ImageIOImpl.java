package com.round.aside.server.module.imageio;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;

import com.round.aside.server.bean.statuscode.ReadImgStatusCodeBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.*;
import static com.round.aside.server.util.MethodUtils.*;

public final class ImageIOImpl implements IImageIO {

    private final BASE64Encoder mBASE64Encoder;

    public ImageIOImpl() {
        mBASE64Encoder = new BASE64Encoder();
    }

    @Override
    public StatusCodeBean writeImg(InputStream mInput, String imgPath) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (mInput == null || StringUtil.isEmpty(imgPath)) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("参数非法").build();
        }
        File file = new File(imgPath);
        File mParentDir = file.getParentFile();
        if (mParentDir.isFile()) {
            mParentDir.delete();
        }
        if (!mParentDir.exists() && !mParentDir.mkdirs()) {
            return mResultBuilder.setStatusCode(R6015).setMsg("目的图片目录文件夹创建失败")
                    .build();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] b = new byte[1024];
            while (mInput.read(b) != -1) {
                out.write(b);
            }

            mResultBuilder.setStatusCode(S1000).setMsg("成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2031).setMsg("目的图片文件错误");
        } catch (IOException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2032).setMsg("文件IO异常");
        } finally {
            closeAutoCloseable(mInput, out);
        }

        return mResultBuilder.build();
    }

    @Override
    public ReadImgStatusCodeBean readImg(String imgpath) {
        ReadImgStatusCodeBean.Builder mResultBuilder = new ReadImgStatusCodeBean.Builder();

        if (StringUtil.isEmpty(imgpath)) {
            mResultBuilder.setStatusCode(ER5001).setMsg("图片路径为空");
            return mResultBuilder.build();
        }

        File file = new File(imgpath);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] b = new byte[(int) file.length()];
            in.read(b);

            mResultBuilder.setBase64ImgData(mBASE64Encoder.encode(b));
            mResultBuilder.setStatusCode(S1000).setMsg("成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2031).setMsg("源图片文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2032).setMsg("源图读取异常");
        } finally {
            closeAutoCloseable(in);
        }

        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean clipImg(String mOriImgPath, String mClipImgPath,
            String mExtension, int mMaxWidth, int mMaxHeight,
            String mClipImgType) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmptyInSet(mOriImgPath, mClipImgPath, mExtension,
                mClipImgType)) {
            mResultBuilder.setStatusCode(ER5001).setMsg("参数非法");
            return mResultBuilder.build();
        }

        if (mMaxWidth <= 0 || mMaxHeight <= 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("裁切宽高大小非法");
            return mResultBuilder.build();
        }

        File mOriImgFile = new File(mOriImgPath);
        if (!mOriImgFile.exists()) {
            mResultBuilder.setStatusCode(R6014).setMsg("源图不存在");
            return mResultBuilder.build();
        }

        File mClipImgFile = new File(mClipImgPath);
        File mClipImgParentDir = mClipImgFile.getParentFile();
        if (mClipImgParentDir.isFile()) {
            mClipImgParentDir.delete();
        }
        if (!mClipImgParentDir.exists() && !mClipImgParentDir.mkdirs()) {
            mResultBuilder.setStatusCode(R6015).setMsg("裁剪图片目录文件夹创建失败");
            return mResultBuilder.build();
        }

        Image mOriImage = null;
        try {
            mOriImage = ImageIO.read(mOriImgFile);
        } catch (IOException e) {
            mResultBuilder.setStatusCode(R6014).setMsg("源图读取失败");
            return mResultBuilder.build();
        }

        BufferedImage mClipBufferedImage = new BufferedImage(mMaxWidth,
                mMaxHeight, BufferedImage.TYPE_INT_RGB);

        mClipBufferedImage.getGraphics().drawImage(
                mOriImage.getScaledInstance(mMaxWidth, mMaxHeight,
                        Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(mClipBufferedImage, mClipImgType, mClipImgFile);

            mResultBuilder.setStatusCode(S1000).setMsg("成功");
        } catch (IOException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2032).setMsg("裁切图写入异常");
        }

        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean zoomImg(String mOriImgPath, String mThumbImgPath,
            String mExtension, int mMaxWidth, int mMaxHeight,
            String mThumbImgType) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmptyInSet(mOriImgPath, mThumbImgPath, mExtension,
                mThumbImgType)) {
            mResultBuilder.setStatusCode(ER5001).setMsg("参数非法");
            return mResultBuilder.build();
        }

        if (mMaxWidth <= 0 || mMaxHeight <= 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("缩略图宽高大小非法");
            return mResultBuilder.build();
        }

        File mOriImgFile = new File(mOriImgPath);
        if (!mOriImgFile.exists()) {
            mResultBuilder.setStatusCode(R6014).setMsg("源图不存在");
            return mResultBuilder.build();
        }

        File mThumbImgFile = new File(mThumbImgPath);
        File mThumbImgParentDir = mThumbImgFile.getParentFile();
        if (mThumbImgParentDir.isFile()) {
            mThumbImgParentDir.delete();
        }
        if (!mThumbImgParentDir.exists() && !mThumbImgParentDir.mkdirs()) {
            mResultBuilder.setStatusCode(R6015).setMsg("缩略图片目录文件夹创建失败");
            return mResultBuilder.build();
        }

        Image mOriImage = null;
        try {
            mOriImage = ImageIO.read(mOriImgFile);
        } catch (IOException e) {
            mResultBuilder.setStatusCode(R6014).setMsg("源图读取失败");
            return mResultBuilder.build();
        }

        BufferedImage mClipBufferedImage = new BufferedImage(mMaxWidth,
                mMaxHeight, BufferedImage.TYPE_INT_RGB);

        mClipBufferedImage.getGraphics().drawImage(
                mOriImage.getScaledInstance(mMaxWidth, mMaxHeight,
                        Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(mClipBufferedImage, mThumbImgType, mThumbImgFile);

            mResultBuilder.setStatusCode(S1000).setMsg("成功");
        } catch (IOException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2032).setMsg("缩略图写入异常");
        }

        return mResultBuilder.build();
    }

}
