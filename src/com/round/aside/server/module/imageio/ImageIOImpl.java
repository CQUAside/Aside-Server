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

import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgClipResultEnum;
import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgRWResultEnum;
import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgZoomResultEnum;

public final class ImageIOImpl implements IImageIO {

    @Override
    public ImgRWResultEnum writeImg(InputStream mInput, String imgPath) {

        if (imgPath == null || mInput == null) {
            return ImgRWResultEnum.UNKNOWN_ERR;
        }
        File file = new File(imgPath);
        try {
            FileOutputStream out = new FileOutputStream(file);
            try {
                byte[] b = new byte[1024];
                while (mInput.read(b) != -1) {
                    out.write(b);
                }
                out.close();
                return ImgRWResultEnum.SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
                return ImgRWResultEnum.STREAM_EXCEPTION;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ImgRWResultEnum.FILE_EXCEPTION;
        }

    }

    @Override
    public ReadImageResultEntity readImg(String imgpath) {
        ReadImageResultEntity entity = new ReadImageResultEntity();
        File file = new File(imgpath);
        try {
            InputStream in = new FileInputStream(file);
            try {
                byte[] b = new byte[(int) file.length()];
                in.read(b);
                in.close();
                String base64code = new BASE64Encoder().encode(b);
                entity.setImgtobase64(base64code);
                entity.setResult(ImgRWResultEnum.SUCCESS);
                return entity;
            } catch (IOException e) {
                entity.setResult(ImgRWResultEnum.STREAM_EXCEPTION);
                entity.setImgtobase64(null);
                return entity;
            }
        } catch (FileNotFoundException e) {
            entity.setResult(ImgRWResultEnum.FILE_EXCEPTION);
            entity.setImgtobase64(null);
            return entity;
        }
    }

    @Override
    public ImgClipResultEnum clipImg(String mOriImgPath, String mThumbImgPath,
            int mMaxWidth, int mMaxHeight) {

        File srcFile = new File(mOriImgPath);
        if (!srcFile.exists()) {
            return ImgClipResultEnum.ORIGINAL_IMG_NOT_EXIST;
        }
        Image srcImg;
        try {
            srcImg = ImageIO.read(srcFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ImgClipResultEnum.ORIGINAL_IMG_RW_EXCEPTION;
        }
        BufferedImage buffImg = null;
        buffImg = new BufferedImage(mMaxWidth, mMaxHeight,
                BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(
                srcImg.getScaledInstance(mMaxWidth, mMaxHeight,
                        Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(buffImg, "JPEG", new File(mThumbImgPath));
        } catch (IOException e) {
            e.printStackTrace();
            return ImgClipResultEnum.CLIPPED_IMG_WRITE_EXCEPTION;
        }
        return ImgClipResultEnum.SUCCESS;

    }

    @Override
    public ImgZoomResultEnum zoomImg(String mOriImgPath, String mThumbImgPath,
            String mExtension, int mMaxWidth, int mMaxHeight) {
        return ImgZoomResultEnum.SUCCESS;
    }

}
