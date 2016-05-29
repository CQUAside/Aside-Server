package com.round.aside.server.module.personalAdsmanager;

import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 用户个人广告管理模块超级接口的实现类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 * 
 */
public class PersonalAdsManagerImpl implements IPersonalAdsManager {

    @Override
    public StatusCodeBean collectAds(int userID, int adID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        PersonalCollectionEntity collection = new PersonalCollectionEntity();
        collection.setAdID(adID);
        collection.setUserID(userID);
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = datamanager
                .insertCollection(collection);
        datamanager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2013:
                mResultBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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
    public StatusCodeBean cancelCollectAds(int userID, int adID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = datamanager.deleteCollecion(adID,
                userID);
        datamanager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2015:
                mResultBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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
    public StatusCodeBean informUser(int userID, int informedUserID,
            String informReason) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        InformUsersEntity inform = new InformUsersEntity();
        inform.setUserID(userID);
        inform.setInformedUserID(informedUserID);
        inform.setInformReason(informReason);
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = datamanager.insertInformUser(inform);
        datamanager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2013:
                mResultBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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
    public StatusCodeBean informAd(int userID, int adID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        InformAdsEntity inform = new InformAdsEntity();
        inform.setAdID(adID);
        inform.setUserID(userID);
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        StatusCodeBean mStatusCodeBean = datamanager.insertInformAd(inform);
        datamanager.release();
        switch (mStatusCodeBean.getStatusCode()) {
            case EX2013:
                mResultBuilder.setStatusCode(EX2000).setMsg("数据库操作异常，请重试");
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

}
