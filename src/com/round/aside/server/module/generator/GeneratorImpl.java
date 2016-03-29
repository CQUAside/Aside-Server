package com.round.aside.server.module.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * ID生成器模块接口的实现类
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public final class GeneratorImpl implements IGenerator {

    private static final String PICID_FORMAT = "%s%8d%4d";

    @Override
    public int generateUserID(int mInitSeed) {
        Random mRandom = new Random(mInitSeed);
        return mRandom.nextInt(100000000);
    }

    @Override
    public String generatePicID(int mUserID, int mInitSeed) {
        Random mRandom = new Random(mInitSeed);

        DateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        String mTimeStr = mDateFormat.format(new Date());
        return String.format(Locale.getDefault(), PICID_FORMAT, mTimeStr, mUserID, mRandom.nextInt(10000));
    }

}
