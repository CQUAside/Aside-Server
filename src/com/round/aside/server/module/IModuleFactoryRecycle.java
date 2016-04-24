package com.round.aside.server.module;

import com.round.aside.server.module.generator.IGenerator;

/**
 * 用于带回收复用功能的模块工厂类继承实现的回调接口
 * 
 * @author A Shuai
 * @date 2016-4-24
 * 
 * @param <T> 类型参数T为可回收复用的模块对象，目前支持的模块类有{@link IGenerator}
 */
public interface IModuleFactoryRecycle<T extends IModule> {

    /**
     * 通知回收指定的模块对象
     * 
     * @param mModule
     *            待回收的模块对象
     */
    void onRecycleModule(T mModule);

}
