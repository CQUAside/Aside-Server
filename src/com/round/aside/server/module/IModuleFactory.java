package com.round.aside.server.module;

/**
 * 模块工厂接口
 * 
 * @author A Shuai
 * 
 * @param <T>
 */
public interface IModuleFactory<T extends IModule> {

    /**
     * 生产模块方法
     * 
     * @param o1
     * @param objects
     * @return
     */
    public T createModule(Object o1, Object... objects);

}
