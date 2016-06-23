package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.entity.PublishAdEntity;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.requestparameter.PublishAdRequestPara;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.admanager.IAdvertisementManager;
import com.round.aside.server.util.StringUtil;

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

        PublishAdRequestPara.Builder mPubAdRPBuilder = new PublishAdRequestPara.Builder();
        mPubAdRPBuilder.fillFieldKey();
        String error = mPubAdRPBuilder.readParameterFromRequest(request);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        error = mPubAdRPBuilder.fillField();
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        PublishAdRequestPara mPubAdRP = mPubAdRPBuilder.build();

        if (!doVerifyTokenInPost(response, mPubAdRP)) {
            return;
        }

        IAdvertisementManager mAdMana = ModuleObjectPool.getModuleObject(
                IAdvertisementManager.class, null);

        PublishAdEntity mPublishAdEntity = new PublishAdEntity(mPubAdRP);
        StatusCodeBean mStatusCodeBean = mAdMana.uploadAD(mPublishAdEntity,
                mPubAdRP.getUserID());

        BaseResultBean mBean = new BaseResultBean.Builder().setStatusCodeBean(
                mStatusCodeBean).build();

        writeResponse(response, mBean);
    }

}
