package com.round.aside.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.bean.requestparameter.GetLocateCityRequestPara;
import com.round.aside.server.util.StringUtil;

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

        GetLocateCityRequestPara.Builder mGetLocationRPBuilder = new GetLocateCityRequestPara.Builder();
        mGetLocationRPBuilder.fillFieldKey();
        String error = mGetLocationRPBuilder.readParameterFromRequest(request);
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        error = mGetLocationRPBuilder.fillField();
        if (!StringUtil.isEmpty(error)) {
            writeErrorResponse(response, ER5001, error);
            return;
        }
        GetLocateCityRequestPara mGetLocationRP = mGetLocationRPBuilder.build();

        if (!doVerifyTokenInPost(response, mGetLocationRP)) {
            return;
        }

    }

}
