package com.round.aside.server.module.imageio;

import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgRWResultEnum;

/**
 * 图片读取结果实体
 * @author Ghost White
 * @date 2016-4-19
 *
 */
public class ReadImageResultEntity {
    
    /**
     * 图片读取结果枚举
     */
    private ImgRWResultEnum result;
    
    /**
     * 图片数据输出流
     */
    private String imgtobase64;

    public ImgRWResultEnum getResult() {
        return result;
    }

    public void setResult(ImgRWResultEnum rusult) {
        this.result = rusult;
    }

    public String getImgtobase64() {
        return imgtobase64;
    }

    public void setImgtobase64(String imgtobase64) {
        this.imgtobase64 = imgtobase64;
    }


}
