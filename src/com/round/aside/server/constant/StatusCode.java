package com.round.aside.server.constant;

import com.round.aside.server.module.accountmanager.IAccountManager;

/**
 * <P>
 * 状态码常量类
 * 
 * <P>
 * 用于枚举出系统中使用的所有常量状态码。
 * 
 * <P>
 * 目前的分类标准为<code>S前缀</code>为<code>Success</code>成功码， <code>EX前缀</code>为
 * <code>Exception</code>异常码， <code>ER前缀</code>为<code>Error</code>错误码，
 * <code>R前缀</code>为<code>Result</code>结果码， <code>F前缀</code>为<code>Fail</code>
 * 失败码， <code>L后缀</code>为<code>Limited</code>有限制使用场景的状态码，其余为通用码。
 * 
 * @author A Shuai
 * @date 2016-3-29
 * 
 */
public final class StatusCode {

    private StatusCode() {
    }

    /**
     * 成功码
     */
    public static final int S1000 = 1000;

    /**
     * 由于一些原因未完成全部检查，只做了部分检查且检查通过
     */
    public static final int S1001 = 1001;

    /**
     * 对于那些带有Token验证的Api，第一步是验证Token，验证通过以后为当前成功码。针对大部分接口，可能只是这一部分成功。
     */
    public static final int S1002 = 1002;

    /**
     * 同类型的操作已成功执行过，无需再次执行
     */
    public static final int S1003 = 1003;

    /**
     * 基础异常码，指API调用过程中出现了异常，但是无需对调用方告知，只需简单通知其发生了异常即可
     */
    public static final int EX2000 = 2000;

    /**
     * 数据库连接异常
     */
    public static final int EX2001 = 2001;

    /**
     * 数据库操作异常，用于包装{@link #EX2012}、{@link #EX2013}、{@link #EX2014}、
     * {@link #EX2015}、{@link #EX2016}类型的异常，实现对外隐藏具体的异常类型。
     */
    public static final int EX2010 = 2010;

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
     * 数据库中受Unique约束的字段插入冲突
     */
    public static final int EX2017 = 2017;

    /**
     * FileNotFoundException文件未发现异常
     */
    public static final int EX2031 = 2031;

    /**
     * IOException IO类型异常
     */
    public static final int EX2032 = 2032;

    /**
     * 基础错误码
     */
    public static final int ER5000 = 5000;

    /**
     * 非法参数错误，即调用参数不合法
     */
    public static final int ER5001 = 5001;

    /**
     * 参数不是一个合法的手机号
     */
    public static final int ER5002 = 5002;

    /**
     * 手机号与验证码不对应
     */
    public static final int ER5003L = 5003;

    /**
     * 手机号与验证码对应，但验证码超时导致无效
     */
    public static final int ER5004L = 5004;

    /**
     * Token验证逻辑中的输入参数UserID参数非法
     */
    public static final int ER5005 = 5005;

    /**
     * Token验证逻辑中的输入参数Token参数非法
     */
    public static final int ER5006 = 5006;

    /**
     * 图片上传错误。图片上传应使用multipart
     */
    public static final int ER5007 = 5007;

    /**
     * 上传的图片格式非法或不支持
     */
    public static final int ER5008 = 5008;

    /**
     * 图片IO异常
     */
    public static final int ER5009 = 5009;

    /**
     * 越权调用，妄图修改不属于自己的数据<br>
     * 一般是平级用户，即调用方和数据持有方为同一权限。
     */
    public static final int ER5010 = 5010;

    /**
     * 越权操作。<br>
     * 即低权限用户妄图操作高权限用户的数据。
     */
    public static final int ER5011 = 5011;

    /**
     * 基础结果码
     */
    public static final int R6000 = 6000;

    /**
     * 用户名命名非法
     */
    public static final int R6001 = 6001;

    /**
     * 用户名已存在
     */
    public static final int R6002 = 6002;

    /**
     * 手机号验证码发送失败
     */
    public static final int R6003 = 6003;

    /**
     * 账号不存在
     */
    public static final int R6004 = 6004;

    /**
     * 登陆密码错误
     */
    public static final int R6005 = 6005;

    /**
     * Token非法
     */
    public static final int R6006 = 6006;

    /**
     * Token失效
     */
    public static final int R6007 = 6007;

    /**
     * DB查询无数据
     */
    public static final int R6008 = 6008;

    /**
     * 邮件发送失败
     */
    public static final int R6009 = 6009;

    /**
     * 认证码不符，认证失败
     */
    public static final int R6010 = 6010;

    /**
     * 邮箱未认证，导致无法继续下一步操作
     */
    public static final int R6011 = 6011;

    /**
     * 邮箱认证失败，无法继续下一步操作
     */
    public static final int R6012 = 6012;

    /**
     * 邮箱认证成功，无法继续下一步操作
     */
    public static final int R6013 = 6013;

    /**
     * 源图片文件不存在或读取失败
     */
    public static final int R6014 = 6014;

    /**
     * 目的图片文件目录创建失败或目的图写入失败
     */
    public static final int R6015 = 6015;

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
     * 广告状态不符合操作要求<br>
     * 
     * 例如{@link IAdvertisementManager#checkAD(int)}
     * 方法审核广告时，若广告所处状态不是未审核状态时则，返回该状态码，表明广告在该状态下不能进行审核操作。
     */
    public static final int F8004 = 8004;

    /**
     * 数据库中受Unique约束的字段插入冲突
     */
    public static final int F8005 = 8005;

}
