package com.round.aside.server.bean.jsonbean.builder;

import javax.validation.constraints.NotNull;

import com.round.aside.server.bean.jsonbean.BaseResultBean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 登陆Msg填充建造者
 * 
 * @author A Shuai
 * @date 2016-5-5
 * 
 */
public class LoginBuilder extends BaseResultBean.Builder {

    @Override
    public @NotNull String getMsg(int statusCode) {
        switch (statusCode) {
            case S1000:
                return "登陆成功";
            case ER5001:
                return "账号或密码输入非法，请检查后重新输入";
            case R6004:
                return "账号不存在";
            case R6005:
                return "密码错误";
            case EX2000:
                return "请重试";
            default:
                throw new IllegalArgumentException("");
        }
    }

}
