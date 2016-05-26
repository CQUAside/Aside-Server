package com.round.aside.server.bean.jsonbean.builder;

import javax.validation.constraints.NotNull;

import com.round.aside.server.bean.jsonbean.BaseResultBean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 获取所设置城市的结果builder建造者
 * 
 * @author A Shuai
 * @date 2016-5-22
 * 
 */
public final class GetLocateCityBuilder extends BaseResultBean.Builder {

    @Override
    public @NotNull String getMsg(int statusCode) {
        switch (statusCode) {
            case S1000:
                return "成功";
            case ER5001:
                return "参数非法";
            default:
                throw new IllegalArgumentException("the statuscode is illegal!");
        }
    }

}
