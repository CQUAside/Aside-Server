package com.round.aside.server.bean.jsonbean.builder;

import static com.round.aside.server.constant.StatusCode.*;

import javax.validation.constraints.NotNull;

import com.round.aside.server.bean.jsonbean.BaseResultBean;

/**
 * Token验证部分的建造者
 * 
 * @author A Shuai
 * @date 2016-5-22
 * 
 */
public class TokenVerifyBuilder extends BaseResultBean.Builder {

    @Override
    public final @NotNull String getMsg(int statusCode) {
        switch (statusCode) {
            case S1002:
                return "Token验证成功";
            case ER5001:
                return "参数非法";
            case ER5005:
                return "UserID参数非法";
            case ER5006:
                return "Token参数非法";
            case EX2000:
                return "SQL异常";
            case R6006:
                return "Token非法，请登录";
            case R6007:
                return "Token过期，请重新登录";
            default:
                throw new IllegalArgumentException("the statuscode is illegal!");
        }
    }

}
