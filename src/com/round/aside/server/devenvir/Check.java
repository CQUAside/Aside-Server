package com.round.aside.server.devenvir;

import com.round.aside.server.bean.requestparameter.UserIDTokenRequestPara;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 检查性质的方法
 * 
 * @author A Shuai
 * @date 2016-6-15
 * 
 */
public final class Check {

    private Check() {
    }

    /**
     * 开发环境中的Token测试
     * 
     * @param mBean
     * @return 结果状态码
     */
    @Deprecated
    public static StatusCodeBean verifyToken(UserIDTokenSCBean mBean) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();
        if (mBean.getUserID() != DevGlobalParameter.ADMIN_USERID) {
            mBuilder.setStatusCode(ER5001).setMsg("UserID非法");
        } else if (!DevGlobalParameter.ADMIN_TOKEN.equals(mBean.getToken())) {
            mBuilder.setStatusCode(R6006).setMsg("Token非法");
        } else {
            mBuilder.setStatusCode(S1002).setMsg("Token验证成功");
        }
        return mBuilder.build();
    }

    /**
     * 在开发环境下的Token验证
     * 
     * @param mUserIDTokenRP
     *            包含UserID和Token的RequestParameter
     * @return 结果状态码
     */
    public static StatusCodeBean verifyToken(
            UserIDTokenRequestPara mUserIDTokenRP) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();
        if (mUserIDTokenRP.getUserID() != DevGlobalParameter.ADMIN_USERID) {
            mBuilder.setStatusCode(ER5001).setMsg("UserID非法");
        } else if (!DevGlobalParameter.ADMIN_TOKEN.equals(mUserIDTokenRP
                .getToken())) {
            mBuilder.setStatusCode(R6006).setMsg("Token非法");
        } else {
            mBuilder.setStatusCode(S1002).setMsg("Token验证成功");
        }
        return mBuilder.build();
    }

}
