package com.round.aside.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.requestparameter.UserOpeAdRequestPara;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.admanager.IAdvertisementManager;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 用户操作广告所用的Servlet。<br>
 * 
 * 包括用户上下架广告，删除广告。
 * 
 * @author A Shuai
 * @date 2016-6-21
 * 
 */
public class UserOperateAdServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 5180024621291913224L;

    /**
     * Constructor of the object.
     */
    public UserOperateAdServlet() {
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

        UserOpeAdRequestPara.Builder mUserOpeAdRPBuilder = new UserOpeAdRequestPara.Builder();
        mUserOpeAdRPBuilder.fillFieldKey();
        String error = mUserOpeAdRPBuilder.readParameterFromRequest(request);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        error = mUserOpeAdRPBuilder.fillField();
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        UserOpeAdRequestPara mUserOpeAdRP = mUserOpeAdRPBuilder.build();

        if (!doVerifyTokenInPost(response, mUserOpeAdRP)) {
            return;
        }

        BaseResultBean.Builder mResultBuilder = new BaseResultBean.Builder();

        IAdvertisementManager mAdMana = ModuleObjectPool.getModuleObject(
                IAdvertisementManager.class, null);

        StatusCodeBean mStatusCodeBean;
        switch (mUserOpeAdRP.getAdStatusOpe()) {
            case PUTAWAY:
                mStatusCodeBean = mAdMana.putawayAD(mUserOpeAdRP.getAdID(),
                        mUserOpeAdRP.getUserID());
                break;
            case UNSHELVE:
                mStatusCodeBean = mAdMana.abolishAD(mUserOpeAdRP.getAdID(),
                        mUserOpeAdRP.getUserID());
                break;
            case DELETE:
                mStatusCodeBean = mAdMana.deleteAD(mUserOpeAdRP.getAdID(),
                        mUserOpeAdRP.getUserID());
                break;
            case CHECK:
                writeErrorResponse(response, ER5001, "广告状态审核操作不支持");
                return;
            default:
                writeErrorResponse(response, ER5001, "广告状态操作非法");
                return;
        }

        mResultBuilder.setStatusCodeBean(mStatusCodeBean);
        writeResponse(response, mResultBuilder.build());
    }

}
