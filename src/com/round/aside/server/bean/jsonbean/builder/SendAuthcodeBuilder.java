package com.round.aside.server.bean.jsonbean.builder;

import javax.validation.constraints.NotNull;

import com.round.aside.server.bean.jsonbean.BaseResultBean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 发送手机验证码请求的响应结果描述信息构建类
 * 
 * @author A Shuai
 * @date 2016-5-4
 *
 */
public final class SendAuthcodeBuilder extends BaseResultBean.Builder {

    @Override
    public @NotNull String getMsg(int statusCode) {

        String msg;
        switch (statusCode) {
            case S1000:
                msg = "已发送";
                break;
            case R6003:
                msg = "验证码生成发送失败！可无需提示用户";
                break;
            case ER5001:
                msg = "手机号码输入为空，请输入";
                break;
            case ER5002:
                msg = "请输入合法的手机号";
                break;
            default:
                throw new IllegalArgumentException("the statuscode is illegal!");
        }
        return msg;
    }

}
