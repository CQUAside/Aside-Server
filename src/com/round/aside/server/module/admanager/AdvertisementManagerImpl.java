package com.round.aside.server.module.admanager;

import static com.round.aside.server.constant.StatusCode.*;
import com.round.aside.server.entity.AdvertisementEntity;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.dbmanager.AdAndStatusCode;
import com.round.aside.server.module.dbmanager.IDatabaseManager;

/**
 * 广告管理模块超级接口的实现类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 */
public class AdvertisementManagerImpl implements IAdvertisementManager {

    @Override
    public int uploadAD(AdvertisementEntity ad) {
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int result = datamanager.insertAD(ad);
        datamanager.release();
        return result;
    }

    @Override
    public int checkAD(int adID) {
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int status = 0;
        AdAndStatusCode ad = new AdAndStatusCode();
        ad = datamanager.queryAD(adID);

        int result;

        if (ad.getStatusCode() != 0)
            result = ad.getStatusCode();
        else {
            status = ad.getStatus();
            if (status == 1 || status == 2 || status == 3) {
                result = F8004;
                datamanager.release();
                return result;
            } else if (status == 0) {
                status = 1;
                ad.setStatus(status);
            }
            result = datamanager.updateAD(adID, ad);
        }

        datamanager.release();
        return result;
    }

    @Override
    public int abolishAD(int adID, int userID) {
        int status = 0;
        int userid = 0;
        int result;

        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        AdAndStatusCode ad = new AdAndStatusCode();
        ad = datamanager.queryAD(adID);
        if (ad.getStatusCode() != 0) {
            result = ad.getStatusCode();
        } else {
            status = ad.getStatus();
            userid = ad.getUserID();
            if (userid != userID) {
                datamanager.release();
                return ER5001;
            }
            if (status == 0 || status == 2 || status == 3) {
                datamanager.release();
                return F8004;
            } else if (status == 1) {
                status = 2;
                ad.setStatus(status);
            }
            result = datamanager.updateAD(adID, ad);
        }
        datamanager.release();
        return result;
    }

    @Override
    public int deleteAD(int adID) {
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        int result = datamanager.deleteAD(adID);
        datamanager.release();
        return result;
    }

    @Override
    public int addAdAttention(int adID, int increaseCount) {
        int collectCount = 0;
        int result;
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        AdAndStatusCode ad = new AdAndStatusCode();
        ad = datamanager.queryAD(adID);
        if (ad.getStatusCode() != 0) {
            result = ad.getStatusCode();
        } else {
            collectCount = ad.getCollectCount();
            collectCount = collectCount + increaseCount;
            ad.setCollectCount(collectCount);
            result = datamanager.updateAD(adID, ad);
        }
        datamanager.release();
        return result;
    }

    @Override
    public int addAdView(int adID, int increaseCount) {
        int clickCount = 0;
        int result;

        AdAndStatusCode ad = new AdAndStatusCode();
        IDatabaseManager datamanager = ModuleObjectPool.getModuleObject(
                IDatabaseManager.class, null);
        ad = datamanager.queryAD(adID);
        if (ad.getStatusCode() != 0) {
            result = ad.getStatusCode();
        } else {
            clickCount = ad.getClickCount();
            clickCount = clickCount + increaseCount;
            ad.setClickCount(clickCount);
            result = datamanager.updateAD(adID, ad);
        }

        datamanager.release();
        return result;
    }

}
