package com.round.aside.server.bean.jsonbean.builder;

import javax.validation.constraints.NotNull;

import com.round.aside.server.bean.jsonbean.BaseResultBean;

import static com.round.aside.server.constant.StatusCode.*;

public final class RegisterBuilder extends BaseResultBean.Builder{

    @Override
    public @NotNull String getMsg(int statusCode) {
        switch(statusCode){
            case S1000:
                return "注册成功";
            case ER5001:
                return "输入参数非法，请重新检查";
            case ER5002:
                return "注册手机号非法";
            case ER5003L:
                return "验证码不符";
            case ER5004L:
                return "验证码过期，请重新获取";
            case EX2000:
                return "请重试";
            case F8003L:
                return "注册账号重复";
            default:
                throw new IllegalArgumentException("the statuscode is illegal!");
        }
    }

}
