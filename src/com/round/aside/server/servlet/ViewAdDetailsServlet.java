package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.jsonbean.result.AdDetailsObjBean;
import com.round.aside.server.bean.requestparameter.AdIDRequestPara;
import com.round.aside.server.bean.statuscode.AdDetailsStatusCodeBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.admanager.IAdvertisementManager;
import com.round.aside.server.util.StringUtil;

/**
 * 查看广告详情的Servlet
 * 
 * @author A Shuai
 * @date 2016-6-23
 * 
 */
public class ViewAdDetailsServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -8046398094343777430L;

    /**
     * Constructor of the object.
     */
    public ViewAdDetailsServlet() {
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

        AdIDRequestPara.Builder mAdIDRPBuilder = new AdIDRequestPara.Builder();
        mAdIDRPBuilder.fillFieldKey();
        String error = mAdIDRPBuilder.readParameterFromRequest(request);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        error = mAdIDRPBuilder.fillField();
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        AdIDRequestPara mAdIDRP = mAdIDRPBuilder.build();

        IAdvertisementManager mAdManager = ModuleObjectPool.getModuleObject(
                IAdvertisementManager.class, null);
        AdDetailsStatusCodeBean mAdDetailsSCB = mAdManager.getAdDetails(mAdIDRP
                .getAdID());

        BaseResultBean.Builder mBuilder = new BaseResultBean.Builder()
                .setStatusCodeBean(mAdDetailsSCB);

        if (mAdDetailsSCB.getStatusCode() == S1000) {
            mBuilder.setObj(new AdDetailsObjBean(mAdDetailsSCB));
        }

        writeResponse(response, mBuilder.build());

    }

}
