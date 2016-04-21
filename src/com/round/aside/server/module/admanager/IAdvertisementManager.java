package com.round.aside.server.module.admanager;

import com.round.aside.server.entity.AdvertisementEntity;
import com.round.aside.server.module.IModule;

/**
 * 广告管理模块的超级接口类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 * 
 */
public interface IAdvertisementManager extends IModule{

    /**
     * 上传一个新的广告即在数据库中插入一个广告数据，插入广告实例的全部信息。
     * 
     * @param ad 一个广告实体
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功即 广告上传成功，{@link #F8001}id字段冲突（包括adID、Thumbnail_ID、CarrouselID），
     *         {@link #EX2012}Connection获取失败异常，
     *         {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}参数非法，其他返回值均为非法值。
     * 
     */
    int uploadAD(AdvertisementEntity ad);

    /**
     * 审核广告（广告状态从未审核到已审核）
     * @param adID
     * 			广告ID，unique值，范围为1-99999999
     * @return  为审核成功，状态：未审核（0）、已审核（1）、下架（2）、过期（3）。
     * 		合法的结果值只有六种，分别为{@link #S1000}成功即 广告审核成功
     *         						  {@link #EX2012}Connection获取失败异常，
     *         						  {@link #EX2014}SQL更新执行异常 ，
     *         						  {@link #F8004}状态不符合审核要求，
     *         						  {@link #EX2016}SQL查询执行异常 以及{@link #ER5001}参数非法，其他返回值均为非法值。
     */
    int checkAD(int adID);


    /**
     * 下架广告（广告状态变为下架（2））
     * @param adID
     * 			广告ID，unique值，范围为1-99999999
     * @param userID
     * 			用户ID，unique值，范围为1-99999999
     * @return  为下架操作成功
     */
    int abolishAD(int adID, int userID);

    /**
     * 删除广告
     * @param adID
     * 			广告ID，unique值，范围为1-99999999
     * @return  为删除成功
     * 合法的结果值只有六种，分别为{@link #S1000}成功即 广告审核成功
     *         						  {@link #EX2012}Connection获取失败异常，
     *         						  {@link #EX2015}SQL删除执行异常 ，
     *         						    以及{@link #ER5001}参数非法，其他返回值均为非法值。
     */
    int deleteAD(int adID);


    /**
     * 增加关注度即收藏量CollectCount
     * @param adID
     * 			广告ID，unique值，范围为1-99999999
     * @param CreaseCount
     * @return 新关注计数，-1为操作失败
     */
    int addAdAttention(int adID, int increaseCount);


    /**
     * 增加广告的浏览量==广告浏览量的计算，，即点击量ClickCount
     * @param adID
     * 			广告ID，unique值
     * @param CreaseCount
     * @return int NewAdView 新的广告浏览量，-1为操作失败
     */
    int addAdView(int adID, int increaseCount);
}
