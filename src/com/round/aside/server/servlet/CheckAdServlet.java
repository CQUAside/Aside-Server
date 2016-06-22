package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.ER5001;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.requestparameter.CheckAdServletRequestPara;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.datastruct.RequestParameterSet;
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

        if (!doVerifyTokenInPost(request, response)) {
            return;
        }

        // 检查登陆的账号权限是不是Admin权限

        RequestParameterSet mParaSet = new RequestParameterSet();
        mParaSet.addKey("adID", true).addKey("adOpe", true)
                .addKey("adFinalState", true);
        String readResult = mParaSet.readParameter(request);
        if (!StringUtil.isEmpty(readResult)) {
            writeErrorResponse(response, ER5001, readResult
                    + " parameter isn't set");
            return;
        }

        CheckAdServletRequestPara.Builder mCheckAdSRPBuilder = new CheckAdServletRequestPara.Builder();
        String error = mCheckAdSRPBuilder.fillField(mParaSet);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }

        BaseResultBean.Builder mResultBuilder = new BaseResultBean.Builder();

        CheckAdServletRequestPara mCheckAdSRP = mCheckAdSRPBuilder.build();
        IAdvertisementManager mAdMana = ModuleObjectPool.getModuleObject(
                IAdvertisementManager.class, null);
        StatusCodeBean mSCB = mAdMana.checkAD(mCheckAdSRP.getAdID(),
                mCheckAdSRP.getAdFinalState());

        mResultBuilder.setStatusCodeBean(mSCB);
        writeResponse(response, mResultBuilder.build());
    }

}
