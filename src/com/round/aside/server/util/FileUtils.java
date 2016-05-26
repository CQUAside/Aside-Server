package com.round.aside.server.util;

/**
 * 文件工具类
 * 
 * @author A Shuai
 * @date 2016-5-25
 * 
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * 根据完整文件名获取其扩展名
     * 
     * @param mFileName
     * @return
     */
    public static final String getFileExtension(String mFileName) {
        String mFileExten = mFileName.substring(mFileName.lastIndexOf(".") + 1,
                mFileName.length());
        return mFileExten.toLowerCase().trim();
    }

}
