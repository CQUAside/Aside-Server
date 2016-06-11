package com.round.aside.server.module.admanager;

import com.round.aside.server.bean.entity.PublishAdEntity;
import com.round.aside.server.bean.statuscode.IncrementSumStatusCodeBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.enumeration.AdStatusEnum;
import com.round.aside.server.module.IModule;

/**
 * 广告管理模块的超级接口类
 * 
 * @author ZhengJiaqin
 * @date 2016-04-18
 * 
 */
public interface IAdvertisementManager extends IModule {

    /**
     * 上传一个新的广告即在数据库中插入一个广告数据，插入广告实例的全部信息。
     * 
     * @param ad
     *            一个广告实体
     * @param mUserID
     *            用户ID
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功即 广告上传成功；{@link #F8001}
     *         id字段冲突（包括adID、Thumbnail_ID、CarrouselID）； {@link #EX2010}
     *         数据库操作异常，请重试 以及{@link #ER5001}参数非法，其他返回值均为非法值。
     * 
     */
    StatusCodeBean uploadAD(PublishAdEntity ad, int mUserID);

    /**
     * 审核广告到指定状态（广告状态从未审核或审核通过状态到审核未通过或审核通过状态）
     * 
     * @param adID
     *            广告ID
     * @param finalState
     *            结果状态，只有审核未通过和审核通过两种状态合法
     * @return 合法的结果值只有六种，分别为{@link #S1000}广告审核成功到指定状态； {@link #EX2010}
     *         数据库操作异常，请重试 ；{@link #F8004}广告已被审核通过无需再次审核；{@link #R6008}
     *         无此adID对应的广告； 以及{@link #ER5001}参数非法，其他返回值均为非法值。
     */
    StatusCodeBean checkAD(int adID, AdStatusEnum finalState);

    /**
     * 下架广告
     * 
     * @param adID
     *            广告ID
     * @param userID
     *            用户ID，自然数
     * @return 结果状态数据bean，分别为{@link #S1000}广告下架成功，{@link #F8004}
     *         广告不处于有效期内，不可执行下架操作；{@link #ER5001}参数非法；{@link #R6008}无此adID对应的广告；
     *         {@link #EX2010}数据库操作异常，请重试；{@link #ER5010}adID广告不属于userID用户管理。
     */
    StatusCodeBean abolishAD(int adID, int userID);

    /**
     * 删除广告
     * 
     * @param adID
     *            广告ID
     * @return 为删除成功 合法的结果值只有六种，分别为{@link #S1000}广告删除成功； {@link #EX2010}
     *         数据库操作异常，请重试以及{@link #ER5001}参数非法，其他返回值均为非法值。
     */
    StatusCodeBean deleteAD(int adID);

    /**
     * 增加关注度即收藏量CollectCount
     * 
     * @param adID
     *            广告ID
     * @param increaseCount
     *            新增关注度，可为正为负
     * @return 新关注度结果包括状态值，分别为{@link #S1000}广告关注或取关成功；{@link #R6008}
     *         无此adID对应的广告数据；{@link #ER5001}参数非法；{@link #EX2010}数据库操作异常，请重试。
     */
    IncrementSumStatusCodeBean addAdAttention(int adID, int increaseCount);

    /**
     * 增加广告的浏览量==广告浏览量的计算，，即点击量ClickCount
     * 
     * @param adID
     *            广告ID
     * @param CreaseCount
     *            新增点击量，必须为正数
     * @return 新浏览量结果包括状态值，分别为{@link #S1000}广告点击量增加成功；{@link #R6008}
     *         无此adID对应的广告数据；{@link #ER5001}参数非法；{@link #EX2010}数据库操作异常，请重试。
     */
    IncrementSumStatusCodeBean addAdClickCount(int adID, int increaseCount);
}
