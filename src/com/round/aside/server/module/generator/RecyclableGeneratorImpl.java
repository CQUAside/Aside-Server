package com.round.aside.server.module.generator;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import sun.misc.BASE64Encoder;

import com.round.aside.server.module.IModuleFactoryRecycleCallback;
import com.round.aside.server.util.MD5Utils;

/**
 * 生成器模块接口的可回收复用的实现类
 * 
 * @author A Shuai
 * @date 2016-4-24
 *
 */
public final class RecyclableGeneratorImpl implements IGenerator{

    private IModuleFactoryRecycleCallback<IGenerator> mRecycleCallback;
    
    private static final String PICID_FORMAT = "%s%10d%4d";
    private static final String ORITOKEN_FORMAT = "%10d-%s-%s-%s";
    
    private final Random mRandom;
    
    private final Date mDate;
    private final DateFormat mDateFormat;
    
    private final BASE64Encoder mBase64En;
    
    public RecyclableGeneratorImpl(){
        mRandom = new Random();
        
        mDate = new Date();
        mDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        
        mBase64En = new BASE64Encoder();
    }
    
    @Override
    public int generateUserID(int mInitSeed) {
        int result = 0;
        do {
            result = UUID.randomUUID().toString().hashCode();
        } while (result == 0);

        return Math.abs(result);
    }

    @Override
    public String generatePicID(int mUserID, int mInitSeed) {
        mRandom.setSeed(System.currentTimeMillis());
        
        mDate.setTime(System.currentTimeMillis());
        String mTimeStr = mDateFormat.format(mDate);
        return String.format(Locale.getDefault(), PICID_FORMAT, mTimeStr, mUserID, mRandom.nextInt(10000));
    }
    
    @Override
    public void release() {
        mRecycleCallback.onRecycleModule(this);
    }

    @Override
    public void registerModuleFactoryRecycle(IModuleFactoryRecycleCallback<IGenerator> mRecycle) {
        mRecycleCallback = mRecycle;
    }

    @Override
    public String generateToken(int mUserID, long mTime, String mOS, String mBrowser) {
        mDate.setTime(mTime);
        String mTemp = MD5Utils.encryptionInfoByMd5(String.format(
                Locale.getDefault(), ORITOKEN_FORMAT, mUserID,
                mDateFormat.format(mDate), mOS, mBrowser));
        try {
            return mBase64En.encode(mTemp.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Don't support utf-8 charset!");
        }
    }

    @Override
    public boolean onReuse() {
        return true;
    }

}
