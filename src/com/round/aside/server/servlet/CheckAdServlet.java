package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.ER5001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.requestparameter.CheckAdRequestPara;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.admanager.IAdvertisementManager;
import com.round.aside.server.util.StringUtil;

/**
 * 管理员审核广告所用的Servlet
 * 
 * @author A Shuai
 * @date 2016-6-21
 * 
 */
public class CheckAdServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7453335282130507028L;

    /**
     * Constructor of the object.
     */
    public CheckAdServlet() {
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

        CheckAdRequestPara.Builder mCheckAdRPBuilder = new CheckAdRequestPara.Builder();
        mCheckAdRPBuilder.fillFieldKey();
        String error = mCheckAdRPBuilder.readParameterFromRequest(request);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        error = mCheckAdRPBuilder.fillField();
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        CheckAdRequestPara mCheckAdRP = mCheckAdRPBuilder.build();

        if (!doVerifyTokenInPost(response, mCheckAdRP)) {
            return;
        }

        // 检查登陆的账号权限是不是Admin权限

        BaseResultBean.Builder mResultBuilder = new BaseResultBean.Builder();

        IAdvertisementManager mAdMana = ModuleObjectPool.getModuleObject(
                IAdvertisementManager.class, null);
        StatusCodeBean mSCB = mAdMana.checkAD(mCheckAdRP.getAdID(),
                mCheckAdRP.getAdFinalState());

        mResultBuilder.setStatusCodeBean(mSCB);
        writeResponse(response, mResultBuilder.build());
    }

}
