package com.round.aside.server.bean.jsonbean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 检查账号合法性调用的结果数据bean
 * 
 * @author A Shuai
 * @date 2016-4-28
 * 
 */
public final class CheckAccountLegalResultBean {

    private final int statusCode;
    private final String msg;

    public CheckAccountLegalResultBean(int statusCode) {
        this.statusCode = statusCode;
        switch (statusCode) {
        case S1000:
            msg = "该账号合法，可注册";
            break;
        case S1001:
            msg = "该账号合法，可注册";
            break;
        case R6001:
            msg = "账号非法，请正确填写";
            break;
        case R6002:
            msg = "账号重复";
            break;
        case ER5001:
            msg = "账号为空";
            break;
        default:
            throw new IllegalArgumentException("");
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

}
