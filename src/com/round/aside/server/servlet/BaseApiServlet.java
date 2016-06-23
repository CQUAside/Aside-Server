package com.round.aside.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.round.aside.server.bean.jsonbean.BaseResultBean;
import com.round.aside.server.bean.requestparameter.UserIDTokenRequestPara;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;
import com.round.aside.server.constant.GlobalParameter;
import com.round.aside.server.devenvir.Check;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.accountmanager.IAccountManager;
import com.round.aside.server.module.netsecurity.INetSecurity;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.*;
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
     * 对Post请求中的UserID和Token验证的完全方法。<br>
     * 已弃用该方法。请使用{@link #doVerifyTokenInPost(UserIDTokenRequestPara)}方法替代。
     * 
     * @param request
     *            客户端发送给服务器的请求
     * @param response
     *            服务器发送给客户端的响应
     * @return true为验证通过，false为验证失败
     */
    @Deprecated
    protected final boolean doVerifyTokenInPost(HttpServletRequest request,
            HttpServletResponse response) {
        BaseResultBean.Builder mResultBuilder = new BaseResultBean.Builder();

        UserIDTokenSCBean mUserIDTokenSCB = readUserIDToken(request);
        if (mUserIDTokenSCB.getStatusCode() != S1000) {
            mResultBuilder.setStatusCodeBean(mUserIDTokenSCB);
            writeResponse(response, mResultBuilder.build());
            return false;
        }

        StatusCodeBean mStatusCodeBean = verifyToken(request, mUserIDTokenSCB);
        if (mStatusCodeBean.getStatusCode() != S1002) {
            mResultBuilder.setStatusCodeBean(mStatusCodeBean);
            writeResponse(response, mResultBuilder.build());
            return false;
        }
        return true;
    }

    /**
     * 读取UserID和Token<br>
     * 已弃用，无需替代方法。
     * 
     * @param request
     *            Servlet请求参数
     * @return 状态码
     */
    @Deprecated
    protected final UserIDTokenSCBean readUserIDToken(HttpServletRequest request) {
        UserIDTokenSCBean.Builder mResultBuilder = new UserIDTokenSCBean.Builder();

        String mUserIDStr = request.getParameter("userid");
        int mUserID;
        try {
            mUserID = Integer.valueOf(mUserIDStr);
        } catch (Exception e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(ER5005).setMsg("UserID参数非法");
            return mResultBuilder.build();
        }
        mResultBuilder.setUserID(mUserID);

        String mToken = request.getParameter("token");
        if (StringUtil.isEmpty(mToken)) {
            mResultBuilder.setStatusCode(ER5006).setMsg("Token参数非法");
            return mResultBuilder.build();
        }
        mResultBuilder.setToken(mToken);

        mResultBuilder.setStatusCode(S1000).setMsg("成功");
        return mResultBuilder.build();
    }

    /**
     * Token合法性检查。用于那些带有Token验证的接口调用，用于验证token的合法性。<br>
     * 已弃用。请使用{@link #verifyToken(UserIDTokenRequestPara)}方法替代。
     * 
     * @param request
     *            HttpRequest请求对象
     * @return 状态码数据bean，分别为{@link #S1002}Token验证通过，{@link #ER5005}userId输入非法，
     *         {@link #ER5006}token输入非法，{@link #EX2010}SQL异常，{@link #5001} 参数非法，
     *         {@link #R6006}token非法，{@link #R6007}token失效。
     */
    @Deprecated
    protected final StatusCodeBean verifyToken(HttpServletRequest request,
            UserIDTokenSCBean mBean) {

        if (GlobalParameter.DEV) {
            return Check.verifyToken(mBean);
        }

        INetSecurity mNetSecurity = ModuleObjectPool.getModuleObject(
                INetSecurity.class, null);
        StatusCodeBean mStatusCodeBean = mNetSecurity.checkTokenLegal(
                mBean.getUserID(), mBean.getToken());
        if (mStatusCodeBean.getStatusCode() != S1000) {
            return mStatusCodeBean;
        }

        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        IAccountManager mAccountManager = ModuleObjectPool.getModuleObject(
                IAccountManager.class, null);
        mStatusCodeBean = mAccountManager.verifyToken(mBean.getUserID(),
                mBean.getToken());
        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
                mBuilder.setStatusCode(S1002).setMsg("Token验证成功");
                break;
            case EX2016:
                mBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case R6006:
            case R6007:
                mBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        return mBuilder.build();
    }

    /**
     * 对Post请求中的UserID和Token验证的完全方法。<br>
     * 
     * @param response
     *            服务器发送给客户端的响应
     * @param mUserIDTokenRP
     *            包含UserID和Token的请求参数集
     * @return true为验证通过，false为验证失败
     */
    protected final boolean doVerifyTokenInPost(HttpServletResponse response,
            UserIDTokenRequestPara mUserIDTokenRP) {
        BaseResultBean.Builder mResultBuilder = new BaseResultBean.Builder();

        StatusCodeBean mStatusCodeBean = verifyToken(mUserIDTokenRP);
        if (mStatusCodeBean.getStatusCode() != S1002) {
            mResultBuilder.setStatusCodeBean(mStatusCodeBean);
            writeResponse(response, mResultBuilder.build());
            return false;
        }
        return true;
    }

    /**
     * Token合法性检查。用于那些带有Token验证的接口调用，用于验证token的合法性。
     * 
     * @param mUserIDTokenRP
     *            包含UserID和Token的请求参数集
     * @return 状态码数据bean，分别为{@link #S1002}Token验证通过，{@link #ER5005}userId输入非法，
     *         {@link #ER5006}token输入非法，{@link #EX2010}SQL异常，{@link #5001} 参数非法，
     *         {@link #R6006}token非法，{@link #R6007}token失效。
     */
    protected final StatusCodeBean verifyToken(
            UserIDTokenRequestPara mUserIDTokenRP) {

        if (GlobalParameter.DEV) {
            return Check.verifyToken(mUserIDTokenRP);
        }

        INetSecurity mNetSecurity = ModuleObjectPool.getModuleObject(
                INetSecurity.class, null);
        StatusCodeBean mStatusCodeBean = mNetSecurity.checkTokenLegal(
                mUserIDTokenRP.getUserID(), mUserIDTokenRP.getToken());
        if (mStatusCodeBean.getStatusCode() != S1000) {
            return mStatusCodeBean;
        }

        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        IAccountManager mAccountManager = ModuleObjectPool.getModuleObject(
                IAccountManager.class, null);
        mStatusCodeBean = mAccountManager.verifyToken(
                mUserIDTokenRP.getUserID(), mUserIDTokenRP.getToken());
        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
                mBuilder.setStatusCode(S1002).setMsg("Token验证成功");
                break;
            case EX2016:
                mBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case R6006:
            case R6007:
                mBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal Status Code!");
        }

        return mBuilder.build();

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
        response.setContentType("text/html");
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
     * 向Servlet客户端写回处理错误的结果的重置方法。
     * 
     * @param response
     *            Servlet响应
     * @param mSCB
     *            状态结果码
     */
    protected final void writeErrorResponse(HttpServletResponse response,
            StatusCodeBean mSCB) {
        writeErrorResponse(response, mSCB.getStatusCode(), mSCB.getMsg());
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
     * 向Servlet响应端写回处理正确的结果的重置方法。
     * 
     * @param response
     *            Servlet响应
     * @param mSCB
     *            结果状态码
     * @param obj
     *            结果对象
     */
    protected final void writeCorrectResponse(HttpServletResponse response,
            StatusCodeBean mSCB, Object obj) {
        writeCorrectResponse(response, mSCB.getStatusCode(), mSCB.getMsg(), obj);
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
