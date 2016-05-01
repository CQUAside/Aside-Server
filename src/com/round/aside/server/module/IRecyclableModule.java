package com.round.aside.server.module;

/**
 * 可回收模块接口，适用于那些创建开销较大且适合回收复用的模块类进行继承，以实现可回收复用的模块类。
 * 
 * @author A Shuai
 * @date 2016-4-23
 * 
 */
public interface IRecyclableModule<T extends IModule> extends IModule {

    /**
     * 开始复用事件回调。用于通知当前复用对象进行必要的初始化。
     * 
     * @return true为初始化成功，可以复用；false为初始化失败，不可复用，需重新构造一个。
     */
    boolean onReuse();

    /**
     * 当可回收模块对象使用完毕后调用该方法释放当前模块对象，便于当模块实现了回收复用逻辑时可进行回收，否则即便模块实现了回收复用逻辑也不能正常回收。
     * <p>
     * 子类可在该方法体内进行必要的清理和还原工作，并最后请务必调用
     * {@link IModuleFactoryRecycleCallback#onRecycleModule(IModule)}
     * 方法通知模块工厂进行回收， 其中的形参IModule为this即可。
     */
    void release();

    /**
     * 向可回收模块类中注册回收工厂回调接口。
     * 
     * @param mRecycle
     *            模块工厂的回收模块对象的回调接口
     */
    void registerModuleFactoryRecycle(IModuleFactoryRecycleCallback<T> mRecycle);

}
