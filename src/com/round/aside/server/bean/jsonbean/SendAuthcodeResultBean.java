package com.round.aside.server.bean.jsonbean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 发送手机验证码调用结果数据bean
 * 
 * @author A Shuai
 * @date 2016-4-30
 *
 */
public final class SendAuthcodeResultBean {

    private final int statusCode;
    private final String msg;

    public SendAuthcodeResultBean(int statusCode) {
        this.statusCode = statusCode;
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
