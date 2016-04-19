package com.round.aside.server.module;

import java.util.HashMap;
import java.util.Map;

import com.round.aside.server.module.VIPCertificate.IVIPCertificate;
import com.round.aside.server.module.VIPCertificate.VIPCertificateModuleFactoryImpl;
import com.round.aside.server.module.accountmanager.AccountManagerModuleFactoryImpl;
import com.round.aside.server.module.accountmanager.IAccountManager;
import com.round.aside.server.module.dbmanager.DatabaseManagerModuleFactoryImpl;
import com.round.aside.server.module.dbmanager.IDatabaseManager;
import com.round.aside.server.module.generator.GeneratorModuleFactoryImpl;
import com.round.aside.server.module.generator.IGenerator;
import com.round.aside.server.module.imageio.IImageIO;
import com.round.aside.server.module.imageio.ImageIOModuleFactoryImpl;
import com.round.aside.server.module.imagepath.IImagePath;
import com.round.aside.server.module.imagepath.ImagePathModuleFactoryImpl;
import com.round.aside.server.module.netsecurity.INetSecurity;
import com.round.aside.server.module.netsecurity.NetSecurityModuleFactoryImpl;

/**
 * 模块对象池，用于对外暴露接口供第三方使用获取模块对象，隐藏了模块实现和工厂实现。
 * 
 * @author A Shuai
 * 
 */
public final class ModuleObjectPool {

    private static final Map<String, IModuleFactory<? extends IModule>> mModuleFactoryMap = 
            new HashMap<String, IModuleFactory<? extends IModule>>();

    static {
        mModuleFactoryMap.put(IAccountManager.class.getSimpleName(), new AccountManagerModuleFactoryImpl());
        mModuleFactoryMap.put(INetSecurity.class.getSimpleName(), new NetSecurityModuleFactoryImpl());
        mModuleFactoryMap.put(IDatabaseManager.class.getSimpleName(), new DatabaseManagerModuleFactoryImpl());
        mModuleFactoryMap.put(IGenerator.class.getSimpleName(), new GeneratorModuleFactoryImpl());
        mModuleFactoryMap.put(IImagePath.class.getSimpleName(), new ImagePathModuleFactoryImpl());
        mModuleFactoryMap.put(IImageIO.class.getSimpleName(), new ImageIOModuleFactoryImpl());
        mModuleFactoryMap.put(IVIPCertificate.class.getSimpleName(), new VIPCertificateModuleFactoryImpl());
    }

    private ModuleObjectPool() {
    }

    /**
     * 获取模块的静态方法
     * 
     * @param mModuleName
     *            待获取模块的类，只可以是{@link IAccountManager}，{@link INetSecurity}
     *            ，除此之外其他的输入均为非法输入
     * @param o1
     *            待获取模块的第一个构造参数，可为null
     * @param objects
     *            待获取模块的除第一个参数以外的其他构造参数，是一个变长参数，可为0个至无限个
     * @return 构造完成的模块对象
     */
    public static <T extends IModule> T getModuleObject(Class<T> mModuleName, Object o1, Object... objects) {
        @SuppressWarnings("unchecked")
        IModuleFactory<T> mModuleFactory = (IModuleFactory<T>) mModuleFactoryMap.get(mModuleName.getSimpleName());
        if (mModuleFactory == null) {
            throw new IllegalArgumentException("The module class isn't a legal class or not register.");
        }
        return mModuleFactory.createModule(o1, objects);
    }

}
