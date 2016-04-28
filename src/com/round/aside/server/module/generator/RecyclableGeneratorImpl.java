package com.round.aside.server.module.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.round.aside.server.module.IModuleFactoryRecycle;

/**
 * 生成器模块接口的可回收复用的实现类
 * 
 * @author A Shuai
 * @date 2016-4-24
 *
 */
public final class RecyclableGeneratorImpl implements IGenerator{

    private IModuleFactoryRecycle<IGenerator> mRecycleCallback;
    
    private static final String PICID_FORMAT = "%s%8d%4d";
    
    private final Random mRandom;
    private final DateFormat mDateFormat;
    
    public RecyclableGeneratorImpl(){
        mRandom = new Random();
        mDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
    }
    
    @Override
    public int generateUserID(int mInitSeed) {
        mRandom.setSeed(mInitSeed);
        return mRandom.nextInt(100000000);
    }

    @Override
    public String generatePicID(int mUserID, int mInitSeed) {
        mRandom.setSeed(mInitSeed);
        
        String mTimeStr = mDateFormat.format(new Date());
        return String.format(Locale.getDefault(), PICID_FORMAT, mTimeStr, mUserID, mRandom.nextInt(10000));
    }
    
    @Override
    public void release() {
        mRecycleCallback.onRecycleModule(this);
    }

    @Override
    public void registerModuleFactoryRecycle(IModuleFactoryRecycle<IGenerator> mRecycle) {
        mRecycleCallback = mRecycle;
    }

}