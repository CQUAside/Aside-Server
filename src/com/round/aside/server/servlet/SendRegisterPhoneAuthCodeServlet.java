package com.round.aside.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.accountmanager.IAccountManager;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 发送注册账号时的手机验证码
 * 
 * @author A Shuai
 * @date 2016-4-30
 * 
 */
public class SendRegisterPhoneAuthCodeServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -4975319543847453195L;

    /**
     * Constructor of the object.
     */
    public SendRegisterPhoneAuthCodeServlet() {
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

        String mPhone = request.getParameter("phone");
        IAccountManager mAccountManager = ModuleObjectPool.getModuleObject(
                IAccountManager.class, null);
        StatusCodeBean mStatusCodeBean = mAccountManager
                .sendPhoneAuthcode(mPhone);
        BaseResultBean.Builder mBuilder = new BaseResultBean.Builder();
        if(mStatusCodeBean.getStatusCode() == S1000){
            mBuilder.setStatusCode(S1000).setMsg("验证码发送成功");
        } else {
            mBuilder.setStatusCodeBean(mStatusCodeBean);
        }

        writeResponse(response, mBuilder.build());
    }

}
