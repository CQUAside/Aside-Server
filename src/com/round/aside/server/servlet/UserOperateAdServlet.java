package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.ER5001;
import static com.round.aside.server.constant.StatusCode.S1000;
import static com.round.aside.server.constant.StatusCode.S1002;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.entity.AdStatusOpeEntity;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;
import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.admanager.IAdvertisementManager;
import com.round.aside.server.util.StringUtil;

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

        BaseResultBean.Builder mResultBuilder = new BaseResultBean.Builder();

        UserIDTokenSCBean mUserIDTokenSCB = readUserIDToken(request);
        if (mUserIDTokenSCB.getStatusCode() != S1000) {
            mResultBuilder.setStatusCodeBean(mUserIDTokenSCB);
            writeResponse(response, mResultBuilder.build());
            return;
        }

        StatusCodeBean mStatusCodeBean = verifyToken(request, mUserIDTokenSCB);
        if (mStatusCodeBean.getStatusCode() != S1002) {
            mResultBuilder.setStatusCodeBean(mStatusCodeBean);
            writeResponse(response, mResultBuilder.build());
            return;
        }

        RequestParameterSet mParaSet = new RequestParameterSet();
        mParaSet.addKey("adID", true).addKey("adOpe", true);
        String error = mParaSet.readParameter(request);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error
                    + " parameter isn't set");
            return;
        }

        AdStatusOpeEntity.Builder mAdSOEBuilder = new AdStatusOpeEntity.Builder();
        error = mAdSOEBuilder.fillField(mParaSet);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }

        AdStatusOpeEntity mAdStatusOpeEntity = mAdSOEBuilder.build();
        IAdvertisementManager mAdMana = ModuleObjectPool.getModuleObject(
                IAdvertisementManager.class, null);

        switch (mAdStatusOpeEntity.getAdStatusOpe()) {
            case PUTAWAY:
                mStatusCodeBean = mAdMana.putawayAD(
                        mAdStatusOpeEntity.getAdID(),
                        mUserIDTokenSCB.getUserID());
                break;
            case UNSHELVE:
                mStatusCodeBean = mAdMana.abolishAD(
                        mAdStatusOpeEntity.getAdID(),
                        mUserIDTokenSCB.getUserID());
                break;
            case DELETE:
                mStatusCodeBean = mAdMana.deleteAD(
                        mAdStatusOpeEntity.getAdID(),
                        mUserIDTokenSCB.getUserID());
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
