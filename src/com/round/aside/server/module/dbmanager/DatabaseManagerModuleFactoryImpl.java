package com.round.aside.server.module.dbmanager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.round.aside.server.module.IModuleFactory;
import com.round.aside.server.module.IModuleFactoryRecycleCallback;

/**
 * 数据库管理模块工厂实现类，新增了复用逻辑。生产者消费者框架。
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public final class DatabaseManagerModuleFactoryImpl implements
        IModuleFactory<IDatabaseManager>,
        IModuleFactoryRecycleCallback<IDatabaseManager> {

    private static final int MAX_CAPACITY = 10;

    private final List<IDatabaseManager> mUseingDBManagerList;
    private final List<IDatabaseManager> mIdleDBManagerList;

    private final Lock mLock;
    private final Condition mReleaseCondition;

    public DatabaseManagerModuleFactoryImpl() {

        mUseingDBManagerList = new LinkedList<IDatabaseManager>();
        mIdleDBManagerList = new LinkedList<IDatabaseManager>();

        mLock = new ReentrantLock();
        mReleaseCondition = mLock.newCondition();

    }

    @Override
    public IDatabaseManager createModule(Object o1, Object... objects) {

        IDatabaseManager mDBManager = null;
        mLock.lock();

        try {
            while (mDBManager == null) {

                while (mIdleDBManagerList.size() > 0) {
                    mDBManager = mIdleDBManagerList.remove(0);
                    if (mDBManager.onReuse()) {
                        break;
                    }
                }

                if (mDBManager == null) {
                    try {
                        mDBManager = new RecyclableDatabaseManagerImpl();
                    } catch (NoAvailableJDBCConnectionException e) {
                        e.printStackTrace();
                    }
                    if (mDBManager == null) {
                        mReleaseCondition.await();
                    } else {
                        mDBManager.registerModuleFactoryRecycle(this);
                    }
                }

                if (mDBManager != null) {
                    mUseingDBManagerList.add(mDBManager);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }

        return mDBManager;
    }

    @Override
    public void onRecycleModule(IDatabaseManager mModule) {

        mLock.lock();
        try {

            mUseingDBManagerList.remove(mModule);

            if (mIdleDBManagerList.size() < MAX_CAPACITY) {
                mIdleDBManagerList.add(mModule);
                mReleaseCondition.signalAll();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }

    }

}
