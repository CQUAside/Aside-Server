package com.round.aside.server.module.admanager;

import static com.round.aside.server.constant.StatusCode.*;
import static com.round.aside.server.enumeration.AdStatusEnum.*;

import com.round.aside.server.bean.entity.AdStatusEntity;
import com.round.aside.server.bean.entity.PublishAdEntity;
import com.round.aside.server.bean.statuscode.AdStatusCodeBean;
import com.round.aside.server.bean.statuscode.IncrementSumStatusCodeBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.enumeration.AdStatusEnum;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;

/**
 * 广告管理模块超级接口的实现类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 */
public class AdvertisementManagerImpl implements IAdvertisementManager {

    @Override
    public StatusCodeBean uploadAD(PublishAdEntity ad, int mUserID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = datamanager.insertAD(ad, mUserID,
                AdStatusEnum.UNREVIEW);
        datamanager.release();

        switch (mStatusCodeBean.getStatusCode()) {
            case S1000:
                mResultBuilder.setStatusCode(S1000).setMsg("广告上传成功");
                break;
            case EX2013:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case F8001:
                mResultBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal status code!");
        }
        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean checkAD(int adID, AdStatusEnum finalState) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (finalState != NOTPASS || finalState != VALID) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("审核广告的结果状态参数非法")
                    .build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        AdStatusCodeBean mAdStatusBean = mDBManager.queryAD(adID);
        switch (mAdStatusBean.getStatusCode()) {
            case S1000:
                mResultBuilder.setStatusCode(S1000).setMsg("广告状态相关信息查询成功");
                break;
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case R6008:
                mResultBuilder.setStatusCodeBean(mAdStatusBean);
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal status code!");
        }

        if (mAdStatusBean.getStatusCode() != S1000) {
            mDBManager.release();
            return mResultBuilder.build();
        }

        AdStatusEnum currentAdStatus = mAdStatusBean.getAdStatus();
        if (currentAdStatus == UNREVIEW || currentAdStatus == NOTPASS) {

            AdStatusEntity.Builder mAdStatusEntityBuilder = new AdStatusEntity.Builder()
                    .setAdStatusCodeBean(mAdStatusBean);
            mAdStatusEntityBuilder.setAdStatusType(finalState.getType());

            StatusCodeBean mStatusBean = mDBManager.updateAD(adID,
                    mAdStatusEntityBuilder.build());

            switch (mStatusBean.getStatusCode()) {
                case S1000:
                    mResultBuilder.setStatusCode(S1000).setMsg("广告状态更新成功");
                    break;
                case EX2014:
                    mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                    break;
                case ER5001:
                    mResultBuilder.setStatusCodeBean(mStatusBean);
                    break;
                default:
                    mDBManager.release();
                    throw new IllegalStateException("Illegal status code!");
            }

        } else {
            mResultBuilder.setStatusCode(F8004).setMsg("广告已被审核通过，无法再次审核");
        }

        mDBManager.release();
        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean abolishAD(int adID, int userID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        if (userID <= 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        AdStatusCodeBean mAdStatusBean = mDBManager.queryAD(adID);
        switch (mAdStatusBean.getStatusCode()) {
            case S1000:
                mResultBuilder.setStatusCode(S1000).setMsg("广告状态相关信息查询成功");
                break;
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case R6008:
                mResultBuilder.setStatusCodeBean(mAdStatusBean);
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal status code!");
        }

        if (mAdStatusBean.getStatusCode() != S1000) {
            mDBManager.release();
            return mResultBuilder.build();
        }

        if (mAdStatusBean.getUserID() != userID) {
            return mResultBuilder.setStatusCode(ER5010)
                    .setMsg(adID + "广告不属于" + userID + "用户").build();
        }

        AdStatusEnum mAdStatus = mAdStatusBean.getAdStatus();
        if (mAdStatus == VALID) {

            AdStatusEntity.Builder mAdStatusEntityBuilder = new AdStatusEntity.Builder()
                    .setAdStatusCodeBean(mAdStatusBean);
            mAdStatusEntityBuilder.setAdStatusType(OFFSHELF.getType());

            StatusCodeBean mStatusBean = mDBManager.updateAD(adID,
                    mAdStatusEntityBuilder.build());

            switch (mStatusBean.getStatusCode()) {
                case S1000:
                    mResultBuilder.setStatusCode(S1000).setMsg("广告下架成功");
                    break;
                case EX2014:
                    mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                    break;
                case ER5001:
                    mResultBuilder.setStatusCodeBean(mStatusBean);
                    break;
                default:
                    mDBManager.release();
                    throw new IllegalStateException("Illegal status code!");
            }

        } else {
            mResultBuilder.setStatusCode(F8004).setMsg("下架广告必须处于有效期内");
        }

        mDBManager.release();
        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean deleteAD(int adID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = mDBManager.deleteAD(adID);
        mDBManager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2015:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case S1000:
            case ER5001:
                mResultBuilder.setStatusCodeBean(mStatusCodeBean);
                break;
            default:
                throw new IllegalStateException("Illegal status code!");
        }
        return mResultBuilder.build();
    }

    @Override
    public IncrementSumStatusCodeBean addAdAttention(int adID, int increaseCount) {
        IncrementSumStatusCodeBean.Builder mResultBuilder = new IncrementSumStatusCodeBean.Builder();

        if (adID < 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("AdID参数非法");
            return mResultBuilder.build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        AdStatusCodeBean mAdStatusBean = mDBManager.queryAD(adID);
        switch (mAdStatusBean.getStatusCode()) {
            case S1000:
                mResultBuilder.setStatusCode(S1000).setMsg("广告状态相关信息查询成功");
                break;
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case R6008:
                mResultBuilder.setStatusCodeBean(mAdStatusBean);
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal status code!");
        }

        if (mAdStatusBean.getStatusCode() != S1000) {
            mDBManager.release();
            return mResultBuilder.build();
        }

        AdStatusEntity.Builder mAdStatusEntityBuilder = new AdStatusEntity.Builder()
                .setAdStatusCodeBean(mAdStatusBean);
        int sum = mAdStatusBean.getCollectCount() + increaseCount;
        mAdStatusEntityBuilder.setCollectCount(sum);
        mResultBuilder.setSum(sum);

        StatusCodeBean mStatusBean = mDBManager.updateAD(adID,
                mAdStatusEntityBuilder.build());
        mDBManager.release();

        switch (mStatusBean.getStatusCode()) {
            case S1000:
                mResultBuilder.setStatusCode(S1000).setMsg(
                        increaseCount > 0 ? "广告关注成功" : "广告取关成功");
                break;
            case EX2014:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
                mResultBuilder.setStatusCodeBean(mStatusBean);
                break;
            default:
                throw new IllegalStateException("Illegal status code!");
        }

        return mResultBuilder.build();
    }

    @Override
    public IncrementSumStatusCodeBean addAdClickCount(int adID,
            int increaseCount) {
        IncrementSumStatusCodeBean.Builder mResultBuilder = new IncrementSumStatusCodeBean.Builder();

        if (increaseCount <= 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("点击量增加值必须为自然数");
            return mResultBuilder.build();
        }

        IDatabaseManager mDBManager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);

        AdStatusCodeBean mAdStatusBean = mDBManager.queryAD(adID);
        switch (mAdStatusBean.getStatusCode()) {
            case S1000:
                mResultBuilder.setStatusCode(S1000).setMsg("广告状态相关信息查询成功");
                break;
            case EX2016:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
            case R6008:
                mResultBuilder.setStatusCodeBean(mAdStatusBean);
                break;
            default:
                mDBManager.release();
                throw new IllegalStateException("Illegal status code!");
        }

        if (mAdStatusBean.getStatusCode() != S1000) {
            mDBManager.release();
            return mResultBuilder.build();
        }

        AdStatusEntity.Builder mAdStatusEntityBuilder = new AdStatusEntity.Builder()
                .setAdStatusCodeBean(mAdStatusBean);
        int sum = mAdStatusBean.getClickCount() + increaseCount;
        mAdStatusEntityBuilder.setClickCount(sum);
        mResultBuilder.setSum(sum);

        StatusCodeBean mStatusBean = mDBManager.updateAD(adID,
                mAdStatusEntityBuilder.build());
        mDBManager.release();

        switch (mStatusBean.getStatusCode()) {
            case S1000:
                mResultBuilder.setStatusCode(S1000).setMsg("点击量增加成功");
                break;
            case EX2014:
                mResultBuilder.setStatusCode(EX2010).setMsg("数据库操作异常，请重试");
                break;
            case ER5001:
                mResultBuilder.setStatusCodeBean(mStatusBean);
                break;
            default:
                throw new IllegalStateException("Illegal status code!");
        }

        return mResultBuilder.build();
    }

}
