package com.round.aside.server.module.imageio;

/**
 * 图片存取、剪裁结果枚举
 * @author Ghost White
 * @date 2016-4-18
 *
 */
public class ImageIOResultEnum {
    
    public enum imgRWResultEnum {
        SUCCESS,FILE_EXCEPTION,STREAM_EXCEPTION,UNKNOWN_ERR
    }
    
    public enum imgClipResultEnum{
        SUCCESS,ORIGINAL_IMG_NOT_EXIST,ORIGINAL_IMG_RW_EXCEPTION,CLIPPED_IMG_WRITE_EXCEPTION
    }

}
