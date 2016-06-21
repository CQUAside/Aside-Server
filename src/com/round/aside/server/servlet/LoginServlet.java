package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.jsonbean.result.UserObjBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.accountmanager.IAccountManager;
import com.round.aside.server.util.HttpRequestUtils;

/**
 * 登陆Servlet
 * 
 * @author A Shuai
 * @date 2016-5-5
 * 
 */
public class LoginServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 3294631977038274088L;

    /**
     * Constructor of the object.
     */
    public LoginServlet() {
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

        String mAccount = request.getParameter("account");
        String mPassword = request.getParameter("password");

        RequestInfoBean mRequestInfoBean = HttpRequestUtils
                .getOSBrowserInfo(request);

        IAccountManager mAccountManager = ModuleObjectPool.getModuleObject(
                IAccountManager.class, null);
        UserIDTokenSCBean mLoginUserBean = mAccountManager.login(mAccount,
                mPassword, 7 * 24 * 60 * 60 * 1000, mRequestInfoBean);

        BaseResultBean.Builder mBuilder = new BaseResultBean.Builder().setStatusCodeBean(mLoginUserBean);

        if (mLoginUserBean.getStatusCode() == S1000) {
            UserObjBean mObjBean = new UserObjBean(mLoginUserBean.getUserID(), mLoginUserBean.getToken());
            mBuilder.setObj(mObjBean);
        }

        writeResponse(response, mBuilder.build());
    }

}
