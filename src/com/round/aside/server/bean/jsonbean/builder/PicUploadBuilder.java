package com.round.aside.server.bean.jsonbean.builder;

import javax.validation.constraints.NotNull;

import com.round.aside.server.bean.jsonbean.BaseResultBean;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 
 * 
 * @author A Shuai
 * @date 2016-5-25
 *
 */
public class PicUploadBuilder extends BaseResultBean.Builder{

    @Override
    public @NotNull
    String getMsg(int statusCode) {
        switch(statusCode){
            
        }
        return null;
    }

}
