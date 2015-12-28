package com.round.aside.server.module;

/**
 * 单例工厂实现类
 * 
 * @author A Shuai
 * 
 * @param <T>
 */
public abstract class SingletonModuleFatory<T extends IModule> implements IModuleFactory<T> {

    public T mInstance;

    public SingletonModuleFatory(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        mInstance = t;
    }

    @Override
    public T createModule(Object o1, Object... objects) {
        return mInstance;
    }

}
