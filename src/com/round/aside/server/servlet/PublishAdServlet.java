package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.UserIDTokenBean;
import com.round.aside.server.bean.entity.PublishAdEntity;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.admanager.IAdvertisementManager;

/**
 * 发布广告Servlet
 * 
 * @author A Shuai
 * @date 2016-6-1
 * 
 */
public class PublishAdServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 4072732191000303810L;

    /**
     * Constructor of the object.
     */
    public PublishAdServlet() {
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

        UserIDTokenBean.Builder mUserIDTokenBuilder = new UserIDTokenBean.Builder();
        StatusCodeBean mStatusCodeBean = readUserIDToken(request,
                mUserIDTokenBuilder);
        if (mStatusCodeBean.getStatusCode() != S1000) {
            BaseResultBean mBean = new BaseResultBean.Builder()
                    .setStatusCodeBean(mStatusCodeBean).build();
            writeResponse(response, mBean);
            return;
        }
        UserIDTokenBean mUserIDTokenBean = mUserIDTokenBuilder.build();

        mStatusCodeBean = verifyToken(request, mUserIDTokenBean);
        if (mStatusCodeBean.getStatusCode() != S1002) {
            BaseResultBean mBean = new BaseResultBean.Builder()
                    .setStatusCodeBean(mStatusCodeBean).build();
            writeResponse(response, mBean);
            return;
        }

        RequestParameterSet mParaSet = new RequestParameterSet();
        mParaSet.addKey("adTitle", true).addKey("adLogoImgID", true)
                .addKey("adImgIDSetStr", true).addKey("adDescription", true)
                .addKey("adAreaSetStr", true).addKey("adStartTime", true)
                .addKey("adEndTime", true).addKey("listPriority", false)
                .addKey("carousel", false);
        String readResult = mParaSet.readParameter(request);
        if (readResult != null) {
            writeErrorResponse(response, ER5001, readResult
                    + " parameter isn't set");
            return;
        }

        PublishAdEntity.Builder mPublishAdBuilder = new PublishAdEntity.Builder();
        readResult = mPublishAdBuilder.fillField(mParaSet);
        if (readResult != null) {
            writeErrorResponse(response, ER5001, readResult);
            return;
        }
        PublishAdEntity mPublishAdEntity = mPublishAdBuilder.build();

        IAdvertisementManager mAdMana = ModuleObjectPool.getModuleObject(
                IAdvertisementManager.class, null);
        mStatusCodeBean = mAdMana.uploadAD(mPublishAdEntity,
                mUserIDTokenBean.getUserID());

        BaseResultBean mBean = new BaseResultBean.Builder().setStatusCodeBean(
                mStatusCodeBean).build();

        writeResponse(response, mBean);
    }

}
