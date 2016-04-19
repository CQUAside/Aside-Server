package com.round.aside.server.module.VIPCertificate;

import com.round.aside.server.module.IModule;

/**
 * VIP认证模块超级接口
 * @author Ghost White
 * @date 2016-4-19
 *
 */
public interface IVIPCertificate extends IModule {
    
    /**
     * 用户发出VIP申请
     * @param userId
     *          用户ID
     * @param entity
     *          申请表数据实体
     * @return
     *          申请结果：true——成功，false——失败
     */
    public boolean applyForVIP(int userId,VIPApplyDataEntity entity);
    
    /**
     * VIP认证审核
     * @param userId
     *          用户ID
     * @return
     *          审核结果：true——成功，false——失败
     */
    public boolean verifyForVIP(int userId);
    
    /**
     * 用户充值
     * @param userId
     *          用户ID
     * @param money
     *          充值金额
     * @return
     *          充值结果：true——成功，false——失败
     */
    public boolean recharge(int userId,int money);

}
