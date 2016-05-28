package com.round.aside.server.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.round.aside.server.bean.StatusCodeBean;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.jsonbean.result.PicUploadResult;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;
import com.round.aside.server.module.generator.IGenerator;
import com.round.aside.server.module.imageio.IImageIO;
import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgRWResultEnum;
import com.round.aside.server.module.imageio.ImageIOResultEnum.ImgZoomResultEnum;
import com.round.aside.server.module.imagepath.IImagePath;
import com.round.aside.server.util.FileUtils;
import com.round.aside.server.util.VerifyUtils;

import static com.round.aside.server.constant.Constants.*;
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

        StatusCodeBean mStatusCodeBean = verifyToken(request);
        if (mStatusCodeBean.getStatusCode() != S1002) {
            BaseResultBean mBean = new BaseResultBean.Builder()
                    .setStatusCodeBean(mStatusCodeBean).build();
            writeResponse(response, mBean);
            return;
        }

        int mUserID = Integer.valueOf(request.getParameter("userid"));

        if (!ServletFileUpload.isMultipartContent(request)) {
            writeErrorResponse(response, ER5007, "图片上传未使用multipart");
            return;
        }

        FileItemFactory mDFIF = new DiskFileItemFactory(1024000, new File(
                ORI_IMG_DIR));
        ServletFileUpload mSFU = new ServletFileUpload(mDFIF);
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
                writeErrorResponse(response, EX2000, "后台数据库异常，请重试");
                return;
            }

            FileItemIterator mFileIterator = mSFU.getItemIterator(request);

            int count = 1;
            List<PicUploadResult> mPURList = new ArrayList<PicUploadResult>(5);

            while (mFileIterator.hasNext()) {
                FileItemStream mFileItemStream = mFileIterator.next();

                if (!mFileItemStream.isFormField()
                        && mFileItemStream.getName().length() > 0) {
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
                                break;
                            case EX2013:
                                mDBManager.rollbackTransaction();
                                writeErrorResponse(response, EX2000,
                                        "后台数据库异常，请重试");
                                return;
                            case ER5001:
                            default:
                                mDBManager.rollbackTransaction();
                                throw new IllegalStateException(
                                        "Illegal Status Code!");
                        }
                    }

                    String mOriPicRelaPath = mImagePath.originalImgPath(
                            mUserID, mPicID, mPicExtenName);
                    String mThuPicRelaPath = mImagePath.thumbImgPath(mUserID,
                            mPicID, mPicExtenName);

                    ImgRWResultEnum mRWResult = mImageIO.writeImg(
                            mFileItemStream.openStream(), ROOT_DIR
                                    + mOriPicRelaPath);
                    switch (mRWResult) {
                        case SUCCESS:
                            break;
                        default:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, ER5009, "原图片存储异常");
                            return;
                    }

                    ImgZoomResultEnum mZoomResult = mImageIO.zoomImg(ROOT_DIR
                            + mOriPicRelaPath, ROOT_DIR + mThuPicRelaPath,
                            mPicExtenName, 200, 200);
                    switch (mZoomResult) {
                        case SUCCESS:
                            break;
                        default:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, ER5009, "缩略图存储异常");
                            return;
                    }

                    mStatusCodeBean = mDBManager.updatePicWithOutAdId(mPicID,
                            count, mOriPicRelaPath, mThuPicRelaPath,
                            mPicExtenName);

                    switch (mStatusCodeBean.getStatusCode()) {
                        case S1000:
                            break;
                        case ER5001:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, ER5001, mStatusCodeBean.getMsg());
                            return;
                        case EX2014:
                            mDBManager.rollbackTransaction();
                            writeErrorResponse(response, EX2000, "后台数据库更新异常，请重试");
                            return;
                        default:
                            mDBManager.rollbackTransaction();
                            throw new IllegalStateException(
                                    "Illegal Status Code!");
                    }

                    PicUploadResult.Builder mPURBuilder = new PicUploadResult.Builder();
                    mPURBuilder.setPicName(mPicName).setOrder(count).setPicId(mPicID);
                    mPURList.add(mPURBuilder.build());

                    count++;
                }

            }

            if(mDBManager.commitTransaction()){
                writeCorrectResponse(response, S1000, "成功", mPURList);
            } else {
                mDBManager.rollbackTransaction();
                writeErrorResponse(response, EX2000, "后台数据库事务提交异常，请重试");
            }

        } catch (FileUploadException e) {
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
