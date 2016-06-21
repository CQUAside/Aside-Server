package com.round.aside.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 获取用户上次设置的城市
 * 
 * @author A Shuai
 * @date 2016-5-22
 * 
 */
public class GetLocateCityServlet extends BaseApiServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2022757181531825635L;

    /**
     * Constructor of the object.
     */
    public GetLocateCityServlet() {
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
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserIDTokenSCBean mUserIDTokenSCB = readUserIDToken(request);
        if (mUserIDTokenSCB.getStatusCode() != S1000) {
            BaseResultBean mBean = new BaseResultBean.Builder()
                    .setStatusCodeBean(mUserIDTokenSCB).build();
            writeResponse(response, mBean);
            return;
        }

        StatusCodeBean mStatusCodeBean = verifyToken(request, mUserIDTokenSCB);
        if (mStatusCodeBean.getStatusCode() != S1002) {
            BaseResultBean mBean = new BaseResultBean.Builder()
                    .setStatusCodeBean(mStatusCodeBean).build();
            writeResponse(response, mBean);
            return;
        }

    }

}
