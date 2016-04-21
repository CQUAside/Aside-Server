package com.round.aside.server.constant;

import com.round.aside.server.module.accountmanager.IAccountManager;

/**
 * <P>状态码常量类
 * 
 * <P>用于枚举出系统中使用的所有常量状态码。
 * 
 * <P>目前的分类标准为<code>S前缀</code>为<code>Success</code>成功码，
 * <code>EX前缀</code>为<code>Exception</code>异常码，
 * <code>ER前缀</code>为<code>Error</code>错误码，
 * <code>F前缀</code>为<code>Fail</code>失败码，
 * <code>L后缀</code>为<code>Limited</code>有限制使用场景的状态码，其余为通用码。
 * 
 * @author A Shuai
 * @date 2016-3-29
 *
 */
public final class StatusCode {

    private StatusCode(){}

    /**
     * 成功码
     */
    public static final int S1000 = 1000;

    
    /**
     * 基础异常码，指API调用过程中出现了异常，但是无需对调用方告知，只需简单通知其发生了异常即可
     */
    public static final int EX2000 = 2000;
    
    /**
     * 数据库连接异常
     */
    public static final int EX2001 = 2001;
    
    /**
     * 从数据库连接池中获取Connection异常
     */
    public static final int EX2012 = 2012;
    
    /**
     * 数据库插入异常
     */
    public static final int EX2013 = 2013;
    
    /**
     * 数据库更新异常
     */
    public static final int EX2014 = 2014;
    
    /**
     * 数据库删除异常
     */
    public static final int EX2015 = 2015;
    
    /**
     * 数据库查询异常
     */
    public static final int EX2016 = 2016;
    
    
    /**
     * 基础错误码
     */
    public static final int ER5000 = 5000;
    
    /**
     * 非法参数错误，即调用参数不合法
     */
    public static final int ER5001 = 5001;
    
    
    /**
     * 基础失败码
     */
    public static final int F8000 = 8000;
    
    /**
     * 数据库中Unique约束的id字段插入失败
     */
    public static final int F8001 = 8001;
    
    /**
     * 数据库中Unique约束的account字段插入失败
     */
    public static final int F8002 = 8002;
    
    /**
     * 账号注册模块中注册账号接口的账号重复，限用于
     * {@link IAccountManager#registerAccount(String, String)}方法和
     * {@link IAccountManager#registerAccount(String, String)}方法
     */
    public static final int F8003L = 8003;
    
    /**
     * 状态不符合审核要求
     * 
     * {@link IAdvertisementManager#checkAD(int)}方法
     * 
     * 审核广告时，若广告所处状态不是未审核状态时则，返回该状态码，表明广告在该状态下不能进行审核操作
     */
    public static final int F8004 = 8004;

}
