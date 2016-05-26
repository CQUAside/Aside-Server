package com.round.aside.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.accountmanager.IAccountManager;
import com.round.aside.server.module.netsecurity.INetSecurity;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.ER5001;
import static com.round.aside.server.constant.StatusCode.ER5005;
import static com.round.aside.server.constant.StatusCode.ER5006;
import static com.round.aside.server.constant.StatusCode.EX2000;
import static com.round.aside.server.constant.StatusCode.EX2016;
import static com.round.aside.server.constant.StatusCode.R6006;
import static com.round.aside.server.constant.StatusCode.R6007;
import static com.round.aside.server.constant.StatusCode.S1000;
import static com.round.aside.server.constant.StatusCode.S1002;
import static com.round.aside.server.util.MethodUtils.*;

/**
 * 基础Api形式的Servlet。其中预定了一些方法以供调用。
 * 
 * @author A Shuai
 * @date 2016-5-22
 * 
 */
public abstract class BaseApiServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -4841445013440562966L;

    /**
     * Token合法性检查。用于那些带有Token验证的接口调用，用于验证token的合法性。
     * 
     * @param request
     *            HttpRequest请求对象
     * @return 结果状态码，分别为{@link #S1002}Token验证通过，{@link #ER5005}userId输入非法，
     *         {@link #ER5006}token输入非法，{@link #EX2000}SQL查询异常，{@link #5001}
     *         参数非法，{@link #R6006}token非法，{@link #R6007}token失效
     * @throws ServletException
     * @throws IOException
     */
    protected final int verifyToken(HttpServletRequest request)
            throws ServletException, IOException {

        String mUserIDStr = request.getParameter("userid");

        int mUserID;
        try {
            mUserID = Integer.valueOf(mUserIDStr);
        } catch (Exception e) {
            e.printStackTrace();

            return ER5005;
        }
        String mToken = request.getParameter("token");
        if (StringUtil.isEmpty(mToken)) {
            return ER5006;
        }

        INetSecurity mNetSecurity = ModuleObjectPool.getModuleObject(
                INetSecurity.class, null);
        int mStatusCode = mNetSecurity.checkTokenLegal(mUserID, mToken);
        if (mStatusCode != S1000) {
            return mStatusCode;
        }

        IAccountManager mAccountManager = ModuleObjectPool.getModuleObject(
                IAccountManager.class, null);
        mStatusCode = mAccountManager.verifyToken(mUserID, mToken);
        switch (mStatusCode) {
            case S1000:
                mStatusCode = S1002;
                break;
            case EX2016:
                mStatusCode = EX2000;
                break;
            case ER5001:
            case R6006:
            case R6007:
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        return mStatusCode;
    }

    /**
     * 写入Servlet的Json响应结果
     * 
     * @param response
     *            Servlet响应
     * @param object
     *            待发送的结果，以JSON字符串形式，不可为空
     */
    protected final void writeResponse(HttpServletResponse response,
            @NotNull Object object) {
        if (response == null || object == null) {
            throw new NullPointerException("the parameter shouldn't be null!");
        }
        response.setCharacterEncoding("utf-8");
        response.setStatus(200);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(JSON.toJSONString(object));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            closeAutoCloseable(out);
        }
    }

    /**
     * 向Servlet响应端写回处理错误的结果。<br>
     * 注：最好只处理错误情况的结果写回。因此只包含状态码和对应的描述信息。
     * 
     * @param response
     *            Servlet响应
     * @param statusCode
     *            错误状态码
     * @param msg
     *            描述信息
     */
    protected final void writeErrorResponse(HttpServletResponse response,
            int statusCode, String msg) {
        BaseResultBean.Builder mBuilder = new BaseResultBean.Builder();
        mBuilder.setStatusCode(statusCode).setMsg(msg);
        writeResponse(response, mBuilder.build());
    }

    /**
     * 向Servlet响应端写回处理正确的结果。<br>
     * 注：最好只处理正确情况的结果写回。因此包含状态码、对应的描述信息以及结果。
     * 
     * @param response
     *            Servlet响应
     * @param statusCode
     *            状态码
     * @param msg
     *            描述信息
     * @param obj
     *            结果对象
     */
    protected final void writeCorrectResponse(HttpServletResponse response,
            int statusCode, String msg, Object obj) {
        BaseResultBean.Builder mBuilder = new BaseResultBean.Builder();
        mBuilder.setStatusCode(statusCode).setMsg(msg).setObj(obj);
        writeResponse(response, mBuilder.build());
    }

}
