package com.round.aside.server.module.generator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.round.aside.server.module.IModuleFactory;
import com.round.aside.server.module.IModuleFactoryRecycle;

/**
 * 生成器模块工厂实现类
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public final class GeneratorModuleFactoryImpl implements IModuleFactory<IGenerator>, IModuleFactoryRecycle<IGenerator> {

    private static final int MAX_CAPACITY = 5;

    private final List<IGenerator> mModuleObjectList;
    private final Lock mLock;

    public GeneratorModuleFactoryImpl() {
        mModuleObjectList = new LinkedList<IGenerator>();
        mLock = new ReentrantLock();
    }

    /**
     * 无回收复用式的构造对象
     */
//    @Override
//    public IGenerator createModule(Object o1, Object... objects) {
//        return new GeneratorImpl();
//    }

    @Override
    public IGenerator createModule(Object o1, Object... objects) {
        if (mModuleObjectList.size() != 0) {
            mLock.lock();
            try {
                if (mModuleObjectList.size() != 0) {
                    IGenerator mGenerator = mModuleObjectList.remove(0);
                    return mGenerator;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mLock.unlock();
            }
        }

        IGenerator mGenerator = new RecyclableGeneratorImpl();
        mGenerator.registerModuleFactoryRecycle(this);
        return mGenerator;
    }

    @Override
    public void onRecycleModule(IGenerator mModule) {
        // 只缓存一定数量对象，超出此限定数量的对象将不再缓存
        if (mModuleObjectList.size() < MAX_CAPACITY) {
            mLock.lock();
            try {
                if (mModuleObjectList.size() < MAX_CAPACITY) {
                    mModuleObjectList.add(mModule);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mLock.unlock();
            }

        }
    }

}
