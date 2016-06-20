package com.round.aside.server.servletexten;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import static com.round.aside.server.util.MethodUtils.*;

/**
 * HttpServletRequestWapper的装饰者类。<br>
 * 使用装饰模式解决mutipart/form-data方式的接受参数问题。<br>
 * 装饰者模式
 * 
 * @author A Shuai
 * @date 2016-6-16
 * 
 */
public final class HttpServletRequestDecorator extends
        HttpServletRequestWrapper {

    private final boolean isMultipart;
    private final Map<String, String[]> mParameterMap = new HashMap<String, String[]>();
    private final List<FileItemStream> mFileItemList = new LinkedList<FileItemStream>();

    public HttpServletRequestDecorator(HttpServletRequest request)
            throws FileUploadException, IOException {
        super(request);
        isMultipart = ServletFileUpload.isMultipartContent(request);

        Map<String, List<String>> mTParaMap = new HashMap<String, List<String>>();
        fillParameter(request, mTParaMap);
        convertListMapToArrayMap(mTParaMap);
    }

    private void fillParameter(HttpServletRequest request,
            Map<String, List<String>> mTParaMap) throws FileUploadException,
            IOException {
        if (!isMultipart) {
            return;
        }

        ServletFileUpload mSFU = new ServletFileUpload();
        FileItemIterator mFileIterator = mSFU.getItemIterator(request);
        while (mFileIterator.hasNext()) {
            FileItemStream mFIS = mFileIterator.next();
            if (mFIS.isFormField()) {
                setFormField(mFIS, mTParaMap);
            } else {
                mFileItemList.add(new FileItemStreamDecorator(mFIS));
            }
        }
    }

    private void setFormField(FileItemStream mFIS,
            Map<String, List<String>> mTParaMap) throws IOException {
        InputStream mInputStream = mFIS.openStream();
        String mKey = mFIS.getFieldName();
        List<String> mValues = mTParaMap.get(mKey);
        if (mValues == null) {
            mValues = new LinkedList<String>();
            mTParaMap.put(mKey, mValues);
        }
        mValues.add(Streams.asString(mInputStream));
    }

    private void convertListMapToArrayMap(Map<String, List<String>> mTParaMap) {
        Iterator<Entry<String, List<String>>> mIterator = mTParaMap.entrySet()
                .iterator();
        while (mIterator.hasNext()) {
            Entry<String, List<String>> ele = mIterator.next();
            String mKey = ele.getKey();
            List<String> mValuesList = ele.getValue();
            mParameterMap.put(mKey,
                    mValuesList.toArray(new String[mValuesList.size()]));
        }
    }

    public boolean isMultipartContent() {
        return isMultipart;
    }

    @Override
    public String getParameter(String name) {
        if (isMultipart) {
            String[] mValues = mParameterMap.get(name);
            if (mValues != null && mValues.length > 0) {
                return mValues[0];
            }
            return null;
        } else {
            return super.getParameter(name);
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (isMultipart) {
            return mParameterMap;
        } else {
            return super.getParameterMap();
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (isMultipart) {
            throw new IllegalStateException(
                    "the multipart servlet request isn't support this method!");
        } else {
            return super.getParameterNames();
        }
    }

    @Override
    public String[] getParameterValues(String name) {
        if (isMultipart) {
            return mParameterMap.get(name);
        } else {
            return super.getParameterValues(name);
        }
    }

    /**
     * 获取上传的文件流集
     * 
     * @return 待保存的文件流集
     */
    public List<FileItemStream> getFileItemStreamList() {
        if (isMultipart) {
            return mFileItemList;
        }
        throw new IllegalStateException(
                "the servlet request isn't belong the multipart type!");
    }

    /**
     * 适配器模式，包装转换原FileItemStream类型输入流不可再读性。
     * 
     * @author A Shuai
     * @date 2016-6-16
     *
     */
    public static class FileItemStreamDecorator implements FileItemStream {

        private final FileItemStream mOriFileItemStream;
        private final byte[] mFileByteArray;

        public FileItemStreamDecorator(FileItemStream mFileItemStream)
                throws IOException {
            mOriFileItemStream = mFileItemStream;

            InputStream mInput = mFileItemStream.openStream();
            ByteArrayOutputStream mOutput = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = mInput.read(b, 0, 1024)) != -1) {
                mOutput.write(b, 0, len);
            }
            mFileByteArray = mOutput.toByteArray();
            closeAutoCloseable(mInput, mOutput);
        }

        @Override
        public FileItemHeaders getHeaders() {
            return mOriFileItemStream.getHeaders();
        }

        @Override
        public void setHeaders(FileItemHeaders arg0) {
            mOriFileItemStream.setHeaders(arg0);
        }

        @Override
        public String getContentType() {
            return mOriFileItemStream.getContentType();
        }

        @Override
        public String getFieldName() {
            return mOriFileItemStream.getFieldName();
        }

        @Override
        public String getName() {
            return mOriFileItemStream.getName();
        }

        @Override
        public boolean isFormField() {
            return mOriFileItemStream.isFormField();
        }

        /**
         * 重写输入流的开发方法。<br>
         * 请使用完毕之后务必将其关闭。
         */
        @Override
        public InputStream openStream() throws IOException {
            return new ByteArrayInputStream(mFileByteArray);
        }

    }

}
