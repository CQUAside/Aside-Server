package com.round.aside.server.constant;

/**
 * <P>状态码常量类
 * 
 * <P>用于枚举出系统中使用的所有常量状态码。
 * 
 * <P>目前的分类标准为<code>S前缀</code>为<code>Success</code>成功码，
 * <code>EX前缀</code>为<code>Exception</code>异常码，
 * <code>ER前缀</code>为<code>Error</code>错误码，
 * <code>F前缀</code>为<code>Fail</code>失败码
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
     * 数据库连接异常
     */
    public static final int EX2000 = 2000;
    
    /**
     * 从数据库连接池中获取Connection异常
     */
    public static final int EX2011 = 2011;
    
    /**
     * 数据库插入异常
     */
    public static final int EX2012 = 2012;
    
    /**
     * 数据库更新异常
     */
    public static final int EX2013 = 2013;
    
    /**
     * 数据库删除异常
     */
    public static final int EX2014 = 2014;
    
    /**
     * 数据库查询异常
     */
    public static final int EX2015 = 2015;
    
    
    /**
     * 数据库因为已经有同值的主键导致新主键插入失败
     */
    public static final int F8001 = 8001;


}
