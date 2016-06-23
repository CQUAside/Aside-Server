package com.round.aside.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;

import com.round.aside.server.bean.jsonbean.result.PicUploadResult;
import com.round.aside.server.bean.requestparameter.PicUploadRequestPara;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;
import com.round.aside.server.module.generator.IGenerator;
import com.round.aside.server.module.imageio.IImageIO;
import com.round.aside.server.module.imagepath.IImagePath;
import com.round.aside.server.servletexten.HttpServletRequestDecorator;
import com.round.aside.server.util.FileUtils;
import com.round.aside.server.util.StringUtil;
import com.round.aside.server.util.VerifyUtils;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 图片上传Servlet
 * 
 * @author A Shuai
 * @date 2016-5-24
 * 
 */
public class PicUploadServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -3062654187421181420L;

    /**
     * Constructor of the object.
     */
    public PicUploadServlet() {
        super();
    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException
     *             if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to
     * post.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpServletRequestDecorator mRequestDecorator;
        try {
            mRequestDecorator = new HttpServletRequestDecorator(request);
        } catch (FileUploadException e1) {
            e1.printStackTrace();
            writeErrorResponse(response, F8000, "文件上传出错");
            return;
        }

        PicUploadRequestPara.Builder mPicUploadRPBuilder = new PicUploadRequestPara.Builder();
        mPicUploadRPBuilder.fillFieldKey();
        String error = mPicUploadRPBuilder.readParameterFromRequest(request);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        error = mPicUploadRPBuilder.fillField();
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        PicUploadRequestPara mPicUploadRP = mPicUploadRPBuilder.build();

        if (!doVerifyTokenInPost(response, mPicUploadRP)) {
            return;
        }

        StatusCodeBean mStatusCodeBean;

        int mUserID = mPicUploadRP.getUserID();

        if (!mRequestDecorator.isMultipartContent()) {
            writeErrorResponse(response, ER5007, "图片上传未使用multipart");
            return;
        }

        List<FileItemStream> mFileItemList = mRequestDecorator
                .getFileItemStreamList();
        IGenerator mGenerator = ModuleObjectPool.getModuleObject(
                IGenerator.class, null);
        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        IImagePath mImagePath = ModuleObjectPool.getModuleObject(
                IImagePath.class, null);
        IImageIO mImageIO = ModuleObjectPool.getModuleObject(IImageIO.class,
                null);
        try {

            if (!mDBManager.beginTransaction()) {
                writeErrorResponse(response, EX2010, "后台数据库异常，请重试");
                return;
            }

            int ordinal = 1;
            List<PicUploadResult> mPURList = new ArrayList<PicUploadResult>(5);

            for (int i = 0; i < mFileItemList.size(); i++) {
                FileItemStream mFileItemStream = mFileItemList.get(i);

                if (mFileItemStream.getName().length() > 0) {
                    String mPicName = mFileItemStream.getName();
                    String mPicExtenName = FileUtils.getFileExtension(mPicName);
                    if (!VerifyUtils.isLegalPicFormat(mPicExtenName)) {
                        writeErrorResponse(response, ER5008,
                                "图片格式非法或不支持，只支持jpg、jpeg和png格式");
                        return;
                    }

                    String mPicID = "";

                    LOOP: while (true) {
                        mPicID = mGenerator.generatePicID(mUserID,
                                System.currentTimeMillis());
                        mStatusCodeBean = mDBManager.insertPicWithPicId(mPicID);
                        switch (mStatusCodeBean.getStatusCode()) {
                            case S1000:
                                break LOOP;
                            case EX2017:
                                continue;
                            case EX2013:
                                mDBManager.rollbackTransaction();
                                writeErrorResponse(response, EX2010,
                                        "后台数据库异常，请重试");
                                return;
                            case ER5001:
                            default:
                                mDBManager.rollbackTransaction();
                                throw new IllegalStateException(
                                        "Illegal Status Code!");
                        }
                    }

                    String mOriPicRelaPath = mImagePath.getOriginalImgPath(
                            mUserID, mPicID, mPicExtenName);
                    String mThuPicRelaPath = mImagePath.getThumbImgPath(
                            mUserID, mPicID, mPicExtenName);

                    mStatusCodeBean = mImageIO.writeImg(
                            mFileItemStream.openStream(), mOriPicRelaPath);
                    switch (mStatusCodeBean.getStatusCode()) {
                        case S1000:
                            break;
                        case ER5001:
                            mDBManager.rollbackTransaction();
                            throw new IllegalStateException(
                                    "Illegal parameter exception!");
                        case R6015:
                        case EX2031:
                        case EX2032:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, mStatusCodeBean);
                            return;
                        default:
                            mDBManager.rollbackTransaction();
                            throw new IllegalStateException(
                                    "Illegal Status Code!");
                    }

                    mStatusCodeBean = mImageIO.zoomImg(mOriPicRelaPath,
                            mThuPicRelaPath, mPicExtenName, 200, 200, "JPEG");
                    switch (mStatusCodeBean.getStatusCode()) {
                        case S1000:
                            break;
                        case ER5001:
                            mDBManager.rollbackTransaction();
                            throw new IllegalStateException(
                                    "Illegal parameter exception!");
                        case R6014:
                        case R6015:
                        case EX2032:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, mStatusCodeBean);
                            return;
                        default:
                            mDBManager.rollbackTransaction();
                            throw new IllegalStateException(
                                    "Illegal Status Code!");
                    }

                    mStatusCodeBean = mDBManager.updatePicWithOutAdId(mPicID,
                            ordinal, mOriPicRelaPath, mThuPicRelaPath,
                            mPicExtenName);

                    switch (mStatusCodeBean.getStatusCode()) {
                        case S1000:
                            break;
                        case ER5001:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, ER5001,
                                    mStatusCodeBean.getMsg());
                            return;
                        case EX2014:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, EX2010,
                                    "后台数据库更新异常，请重试");
                            return;
                        default:
                            mDBManager.rollbackTransaction();
                            throw new IllegalStateException(
                                    "Illegal Status Code!");
                    }

                    PicUploadResult.Builder mPURBuilder = new PicUploadResult.Builder();
                    mPURBuilder.setPicName(mPicName).setOrdinal(ordinal)
                            .setPicID(mPicID);
                    mPURList.add(mPURBuilder.build());

                    ordinal++;
                }

            }

            if (mDBManager.commitTransaction()) {
                writeCorrectResponse(response, S1000, "成功", mPURList);
            } else {
                mDBManager.rollbackTransaction();
                writeErrorResponse(response, EX2010, "后台数据库事务提交异常，请重试");
            }

        } catch (IOException e) {
            e.printStackTrace();
            mDBManager.rollbackTransaction();
            writeErrorResponse(response, F8000, "文件上传出错");
            return;
        } finally {
            mDBManager.closeTransaction();
            mGenerator.release();
            mDBManager.release();
        }

    }

}
