package com.round.aside.server.module.personalAdsmanager;

import com.round.aside.server.bean.statuscode.StatusCodeBean;
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
     *            用户ID，unique值
     * @param adID
     *            广告ID，unique值
     * @return 操作结果状态值，分别为{@link #S1000}成功即 收藏广告成功、{@link #EX2000}SQL操作异常 以及
     *         {@link #ER5001}参数非法。
     */
    StatusCodeBean collectAds(int userID, int adID);

    /**
     * 取消收藏广告
     * 
     * @param userID
     *            用户ID，unique值
     * @param adID
     *            广告ID，unique值
     * @return 操作结果状态值，分别为{@link #S1000}成功即表示取消收藏成功、{@link #EX2000}SQL操作异常 以及
     *         {@link #ER5001}参数非法。
     */
    StatusCodeBean cancelCollectAds(int userID, int adID);


    /**
     * 举报用户
     * 
     * @param userID
     *            用户ID，unique值
     * @param reportedUser
     *            被举报用户ID，unique值
     * @param comment
     *            举报原因
     * @return 操作结果状态值，分别为{@link #S1000}成功即 举报用户成功、{@link #EX2000}SQL执行异常 以及
     *         {@link #ER5001}参数非法。
     */
    StatusCodeBean informUser(int userID, int informedUserID,
            String informReason);

    /**
     * 举报广告
     * 
     * @param userID
     *            用户ID
     * @param adID
     *            广告ID
     * @return操作结果状态值，分别为{@link #S1000}成功即 举报广告成功、{@link #EX2000}SQL执行异常 以及
     *                          {@link #ER5001}参数非法。
     */
    StatusCodeBean informAd(int userID, int adID);
}
