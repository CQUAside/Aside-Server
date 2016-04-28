package com.round.aside.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.round.aside.server.bean.jsonbean.CheckAccountLegalResultBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.accountmanager.IAccountManager;

/**
 * 检查用户注册输入账号合法性的Servlet
 * 
 * @author A Shuai
 * @date 2016-4-27
 * 
 */
public final class CheckAccountLegalServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -5849633514871567121L;

    /**
     * Constructor of the object.
     */
    public CheckAccountLegalServlet() {
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String mAccount = request.getParameter("account");

        IAccountManager mAccountManager = ModuleObjectPool.getModuleObject(IAccountManager.class, null);

        int mStatusCode = mAccountManager.checkRegisteredAccountLegal(mAccount);
        CheckAccountLegalResultBean mBean = new CheckAccountLegalResultBean(mStatusCode);

        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println(JSONObject.toJSONString(mBean));

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
