package com.round.aside.server.module;

import com.round.aside.server.module.accountmanager.IAccountManager;
import com.round.aside.server.module.netsecurity.INetSecurity;


/**
 * 模块工厂接口。一般而言模块工厂只会实例化一个对象，并使这个对象提供多线程并发调用
 * 
 * @author A Shuai
 * 
 * @param <T> 类型参数，为待生产对象，可以是{@link IAccountManager},{@link INetSecurity}
 */
public interface IModuleFactory<T extends IModule> {

    /**
     * 模块生产方法。请注意该方法在一般意义下存在多线程并发调用的可能，请注意必要的同步。
     * 
     * @param o1
     *            待生产对象的第一个形参，使用者根据需求进行传参即可，注意如果一个待生产对象无需传参时，o1可为null
     * @param objects
     *            待生产对象的除第一个形参以外的参数，为不定长参数
     * @return 生产产品对象
     */
    public T createModule(Object o1, Object... objects);

}
