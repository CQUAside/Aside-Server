package com.round.aside.server.bean.jsonbean.builder;

import javax.validation.constraints.NotNull;

import com.round.aside.server.bean.jsonbean.BaseResultBean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 检查注册账号合法性请求的响应结果描述信息构建类
 * 
 * @author A Shuai
 * @date 2016-5-4
 *
 */
public final class CheckAccountLegalBuilder extends BaseResultBean.Builder {

    @Override
    public @NotNull String getMsg(int statusCode) {
        String msg;
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
                throw new IllegalArgumentException("the statuscode is illegal!");
        }
        return msg;
    }

}
