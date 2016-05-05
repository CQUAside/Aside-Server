package com.round.aside.server.servlet;

import static com.round.aside.server.constant.StatusCode.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.jsonbean.UserObjBean;
import com.round.aside.server.bean.jsonbean.builder.LoginBuilder;
import com.round.aside.server.entity.LoginUserEntity;
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
public class LoginServlet extends HttpServlet {

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
        LoginUserEntity mLoginUserEntity = mAccountManager.login(mAccount,
                mPassword, 7 * 24 * 60 * 60 * 1000, mRequestInfoBean);

        BaseResultBean.Builder mBuilder = new LoginBuilder()
                .setStatusCode(mLoginUserEntity.getStatuscode());

        if (mLoginUserEntity.getStatuscode() == S1000) {
            UserObjBean mObjBean = new UserObjBean(
                    mLoginUserEntity.getUserID(), mLoginUserEntity.getToken());
            mBuilder.setObj(mObjBean);
        }

        BaseResultBean mBean = mBuilder.build();

        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println(JSON.toJSONString(mBean));
        out.flush();
        out.close();
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

}
