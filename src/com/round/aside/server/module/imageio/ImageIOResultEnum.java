package com.round.aside.server.module.imageio;

/**
 * 图片存取、剪裁结果枚举
 * @author Ghost White
 * @date 2016-4-18
 *
 */
public class ImageIOResultEnum {

    /**
     * 
     * 图片存取结果枚举：
     * 
     * SUCCESS——图片写入成功；
     * FILE_EXCEPTION——文件异常；
     * STREAM_EXCEPTION——流异常；
     * UNKNOWN_ERR——未知错误；
     *
     */
    public enum imgRWResultEnum {
        SUCCESS,FILE_EXCEPTION,STREAM_EXCEPTION,UNKNOWN_ERR
    }
    
    
    /**
     * 图片剪裁结果枚举:
     * SUCCESS:成功；
     * ORIGINAL_IMG_NOT_EXIST:源图片不存在；
     * ORIGINAL_IMG_RW_EXCEPTION：源图片写入异常；
     * CLIPPED_IMG_WRITE_EXCEPTION：剪裁后图片写入异常；
     *
     */
    public enum imgClipResultEnum{
        SUCCESS,ORIGINAL_IMG_NOT_EXIST,ORIGINAL_IMG_RW_EXCEPTION,CLIPPED_IMG_WRITE_EXCEPTION
    }

}
