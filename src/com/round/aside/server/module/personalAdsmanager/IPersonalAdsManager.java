package com.round.aside.server.module.personalAdsmanager;

import com.round.aside.server.module.IModule;

/**
 * 用户个人广告管理模块超级接口类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 *
 */
public interface IPersonalAdsManager extends IModule{

    /**
     * 收藏广告
     * 
     * @param UsrID
     * 			用户ID，unique值
     * @param adID
     * 			广告ID，unique值
     * @return 操作结果状态值，分别为{@link #S1000}成功即 收藏广告成功
     *         {@link #EX2015}SQL删除执行异常 以及{@link #ER5001}参数非法等
     */
    int collectAds(int userID, int adID);

    /**
     * 取消收藏广告
     * 
     * @param userID
     * 			用户ID，unique值
     * @param adID
     * 			广告ID，unique值
     * @return  操作结果状态值，分别为{@link #S1000}成功即表示取消收藏成功
     *         {@link #EX2015}SQL删除执行异常 以及{@link #ER5001}参数非法等
     */
    int cancelCollectAds(int userID, int adID);


    /**
     * 举报用户
     * @param userID
     * 			用户ID，unique值
     * @param reportedUser
     * 			被举报用户ID，unique值
     * @param comment
     * 			举报原因
     * @return 操作结果状态值，分别为{@link #S1000}成功即 举报用户成功
     *         {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}参数非法等
     */
    int informUser(int userID, int informedUserID, String informReason);

    /**
     * 举报广告
     * 
     * @param userID
     * 			用户ID
     * @param adID 
     * 			广告ID
     * @return操作结果状态值，分别为{@link #S1000}成功即 举报广告成功
     *         {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}参数非法等
     */
    int informAd(int userID, int adID);
}
