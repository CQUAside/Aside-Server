package com.round.aside.server.module.personalAdsmanager;

import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.IDatabaseManager;

/**
 * 用户个人广告管理模块超级接口的实现类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 * 
 */
public class PersonalAdsManagerImpl implements IPersonalAdsManager {

    @Override
    public int collectAds(int userID, int adID) {
        PersonalCollectionEntity collection = new PersonalCollectionEntity();
        collection.setAdID(adID);
        collection.setUserID(userID);
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int result = datamanager.insertCollection(collection);
        datamanager.release();
        return result;
    }

    @Override
    public int cancelCollectAds(int userID, int adID) {
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int result = datamanager.deleteCollecion(adID, userID);
        datamanager.release();
        return result;
    }

    @Override
    public int informUser(int userID, int informedUserID, String informReason) {
        InformUsersEntity inform = new InformUsersEntity();
        inform.setUserID(userID);
        inform.setInformedUserID(informedUserID);
        inform.setInformReason(informReason);
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int result = datamanager.insertInformUser(inform);
        datamanager.release();
        return result;
    }

    @Override
    public int informAd(int userID, int adID) {
        InformAdsEntity inform = new InformAdsEntity();
        inform.setAdID(adID);
        inform.setUserID(userID);
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int result = datamanager.insertInformAd(inform);
        datamanager.release();
        return result;
    }

}
