package com.round.aside.server.module.dbmanager;

import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.entity.AdStatusEntity;
import com.round.aside.server.bean.entity.PublishAdEntity;
import com.round.aside.server.bean.statuscode.AdStatusCodeBean;
import com.round.aside.server.bean.statuscode.AuthCodeStatusCodeBean;
import com.round.aside.server.bean.statuscode.EmailStatusCodeBean;
import com.round.aside.server.bean.statuscode.UserIDTokenSCBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.bean.statuscode.UserEmailAuthStatusBean;
import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;
import com.round.aside.server.enumeration.AdStatusEnum;
import com.round.aside.server.enumeration.UserEmailStatusEnum;

import com.round.aside.server.module.IModule;
import com.round.aside.server.module.IRecyclableModule;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 数据库管理模块超级接口
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public interface IDatabaseManager extends IModule,
        IRecyclableModule<IDatabaseManager> {

    /**
     * 检查指定的用户名是否已存在
     * 
     * @param mAccount
     *            待检查的用户名
     * @return 表征结果的状态码，分别有{@link #S1000}SQL执行正常且未重复，{@link #R6002}SQL执行正常且已重复，
     *         {@link #EX2016}SQL查询执行异常， {@link #ER5001}调用参数非法。
     */
    StatusCodeBean checkAccountExistence(String mAccount);

    /**
     * 暂存注册账号步骤中手机号码及对应验证码
     * 
     * @param mPhone
     *            注册手机号
     * @param mAuthcode
     *            对应验证码
     * @return 表征结果的状态码，分别有分别有{@link #S1000}SQL执行正常，{@link #EX2013}SQL插入执行异常，
     *         {@link #ER5001}调用参数非法。
     */
    StatusCodeBean stashRegisterPhoneAuthcode(String mPhone, String mAuthcode,
            long pastdueTime);

    /**
     * 检查注册手机号和验证码是否对应
     * 
     * @param mPhone
     *            手机号
     * @param mAuthcode
     *            验证码
     * @return 结果状态码，分别有{@link #ER5001}调用参数非法，{@link #EX2016}SQL查询执行异常，
     *         {@link #ER5003L}手机号和验证码不对应，{@link #ER5004L}手机号与验证码对应，但验证码超时无效，
     *         {@link #S1000}成功
     */
    StatusCodeBean checkRegisterPhoneAuthcode(String mPhone, String mAuthcode,
            long currentTime);

    /**
     * 新插入一条用户信息记录，但只插入用户ID，账号和密码三个字段，其余字段使用Update方法进行更新式填充
     * 
     * @param mUserID
     *            用户ID，Unique值，范围为大于0的int型正整数
     * @param mAccount
     *            用户账号，Unique值，不能为空
     * @param mPassword
     *            用户密码，不能为空
     * @param mPhone
     *            用户电话
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功，{@link #F8001}userid字段冲突，
     *         {@link #F8002}account字段冲突， {@link #EX2013}SQL插入执行异常 以及
     *         {@link #ER5001}调用参数非法，其他返回值均为非法值。
     */
    StatusCodeBean insertUser(int mUserID, String mAccount, String mPassword,
            String mPhone);

    /**
     * 插入一条token记录，作为登陆成功的凭证
     * 
     * @param mUserID
     *            用户ID
     * @param mRequestInfoBean
     *            用户的相关本地信息
     * @param mToken
     *            令牌
     * @param loginTime
     *            登陆时间
     * @param pastdueTime
     *            令牌过期时间
     * @return 状态码，分别有{@link #S1000}token插入成功，{@link #ER5001}调用参数非法，
     *         {@link #EX2013}数据库插入异常。
     */
    StatusCodeBean insertToken(int mUserID, RequestInfoBean mRequestInfoBean,
            String mToken, long loginTime, long pastdueTime);

    /**
     * 普通登陆检查
     * 
     * @param mAccount
     *            账号
     * @param mPassword
     *            密码
     * @param period
     *            申请token有效期
     * @return 包含状态码和用户登录信息的数据bean，状态码分别为{@link #S1000}账号密码验证通过，{@link #ER5001}
     *         调用参数非法，{@link #R6004}账号不存在，{@link #R6005}密码错误，{@link #EX2016}
     *         SQL查询异常。
     */
    UserIDTokenSCBean loginCheck(String mAccount, String mPassword, long period);

    /**
     * Token令牌登陆检查
     * 
     * @param userId
     *            用户ID
     * @param token
     *            令牌
     * @return 结果状态码，有且仅有以下几种，分别为{@link #S1000}合法，{@link #ER5001}调用参数非法，
     *         {@link #R6006}Token非法，{@link #R6007}Token失效，{@link #EX2016}
     *         SQL查询执行异常。
     */
    StatusCodeBean tokenLoginCheck(int userId, String token);

    /**
     * 插入一条图片记录，但只插入PicId字段，以避免PicId重复
     * 
     * @param picId
     *            图片ID
     * @return 状态码及其对应描述，状态码分别为{@link #S1000}成功，{@link #ER5001}图片ID参数非法，
     *         {@link #EX2017}PicID不满足Unique约束，{@link #EX2013}数据库插入异常。
     */
    StatusCodeBean insertPicWithPicId(String picId);

    /**
     * 更新图片记录的大部分字段，暂不更新广告id。
     * 
     * @param picId
     *            图片ID
     * @param ordinal
     *            序号
     * @param originalPath
     *            原图相对路径
     * @param thumbPath
     *            缩略图相对路径
     * @param extension
     *            文件扩展名
     * @return 结果状态码及其对应描述，有且仅有以下几种，分别为{@link #S1000}成功，{@link #ER5001}调用参数非法，
     *         {@link #EX2014}数据库更新异常。
     */
    StatusCodeBean updatePicWithOutAdId(String picId, int ordinal,
            String originalPath, String thumbPath, String extension);

    /**
     * 新增加一个广告信息记录，插入广告信息的全部字段
     * 
     * @param ad
     *            广告实体
     * @param mUserID
     *            用户ID
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功即 广告插入成功，{@link #F8001}，
     *         {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}参数非法，其他返回值均为非法值。
     */
    StatusCodeBean insertAD(PublishAdEntity ad, int mUserID,
            AdStatusEnum mAdStatusEnum);

    /**
     * 根据广告ID，查询一条广告信息记录
     * 
     * @param adID
     *            广告ID
     * @return AdAndStatusCode
     *         一个部分广告状态信息加上返回状态码的一个实体（广告信息包括：广告状态、广告收藏量、广告点击量、用户ID）<br>
     *         状态码分别为{@link #S1000}查询成功，{@link #5001}所传参数非法，{@link #6008}
     *         adID非法，无此数据，{@link #EX2016}数据库查询异常
     */
    AdStatusCodeBean queryAD(int adID);

    /**
     * 更新广告记录的某些字段（广告状态或广告收藏量或广告点击量）
     * 
     * @param adID
     *            广告ID
     * @param ad
     *            一个部分广告信息加上返回状态码的一个实体（广告信息包括：广告状态、广告收藏量、广告点击量、用户ID）
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功即 广告更新成功 {@link #EX2014}
     *         SQL插入执行异常 以及{@link #ER5001}参数非法，其他返回值均为非法值。
     */
    StatusCodeBean updateAD(int adID, AdStatusEntity ad);

    /**
     * 根据广告ID删除一个广告
     * 
     * @param adID
     *            广告ID
     * @return 操作结果状态值，分别为{@link #S1000}成功即 广告删除成功 {@link #EX2015}SQL删除执行异常 以及
     *         {@link #ER5001}参数非法等
     */
    StatusCodeBean deleteAD(int adID);

    /**
     * 插入一个用户收藏广告的记录
     * 
     * @param personalCollection
     *            收藏实体
     * @return 操作结果状态值，分别为{@link #S1000}成功即 插入收藏记录成功、{@link #EX2013}SQL插入执行异常 以及
     *         {@link #ER5001}参数非法等
     */
    StatusCodeBean insertCollection(PersonalCollectionEntity personalCollection);

    /**
     * 根据用户ID和广告ID删除一个用户收藏广告的记录
     * 
     * @param adID
     *            广告ID
     * @param userID
     *            用户ID
     * @return 操作结果状态值，分别为{@link #S1000}成功即 删除收藏记录成功、{@link #EX2015}SQL删除执行异常 以及
     *         {@link #ER5001}参数非法等
     */
    StatusCodeBean deleteCollecion(int adID, int userID);

    /**
     * 用户举报广告时，插入一个举报广告记录
     * 
     * @param informAd
     *            举报广告实体（包括用户ID，广告ID...）
     * @return 操作结果状态值，分别为{@link #S1000}成功即 插入举报记录成功、{@link #EX2013}SQL插入执行异常 以及
     *         {@link #ER5001}参数非法。
     */
    StatusCodeBean insertInformAd(InformAdsEntity informAd);

    /**
     * 用户举报用户时，插入一个举报用户记录
     * 
     * @param informUser
     *            举报用户实体（包括举报人ID，别举报人ID，举报原因....）
     * @return 操作结果状态值，分别为{@link #S1000}成功即 插入举报记录成功、{@link #EX2013}SQL插入执行异常 以及
     *         {@link #ER5001}参数非法。
     */
    StatusCodeBean insertInformUser(InformUsersEntity informUser);

    /**
     * 写入用户邮件认证码
     * 
     * @param mUserID
     *            用户ID
     * @param mEmail
     *            邮箱地址
     * @param mAuthCode
     *            用户验证邮箱生成的验证码
     * @param pastdueTime
     *            过期时间
     * @return 操作结果状态值 ，有四种{@link #S1000}成功，{@link #EX2014}SQL更新执行异常，
     *         {@link #ER5001}调用参数非法，其他返回值均为非法值
     */
    StatusCodeBean insertUserEmailAuthCode(int mUserID, String mEmail,
            String mAuthCode, long pastdueTime);

    /**
     * 查询用户邮件认证码
     * 
     * @param mUserID
     *            用户ID
     * @param mEmail
     *            邮箱
     * @return 认证码结果，包含状态码。状态值分别为{@link #S1000}查询成功，{@link #R6008}为无参数对应的数据，
     *         {@link #ER5001}参数错误，{@link #EX2016}数据库查询异常。
     */
    AuthCodeStatusCodeBean getUserEmailAuthCode(int mUserID, String mEmail);

    /**
     * 查询用户验证邮箱状态
     * 
     * @param mUserID
     *            用户ID
     * @return 用户邮箱验证结果，包含状态码，状态值分别有{@link #S1000}查询成功，{@link #ER5001}参数错误，
     *         {@link #R6008}无此UserID数据，{@link #EX2016}数据库查询异常。
     */
    UserEmailAuthStatusBean selectUserEmailStatus(int mUserID);

    /**
     * 更新用户邮箱认证状态为指定状态
     * 
     * @param mUserID
     *            用户ID
     * @param mEmailStatus
     *            用户邮箱认证状态，只可以是认证成功或认证失败两种状态
     * @return 结果状态码
     */
    StatusCodeBean updateUserEmailStatus(int mUserID,
            UserEmailStatusEnum mEmailStatus);

    /**
     * 查询指定UserID对应用户的Email
     * 
     * @param mUserID
     *            用户ID
     * @return 用户Email结果，包含状态码，状态值分别有{@link #S1000}查询成功，{@link #ER5001}参数错误，
     *         {@link #R6008}无此UserID数据，{@link #EX2016}数据库查询异常。
     */
    EmailStatusCodeBean selectUserEmail(int mUserID);

    /**
     * 写入邮件找回密码步骤中的认证码
     * 
     * @param mUserID
     *            用户ID
     * @param mEmail
     *            邮箱地址
     * @param mAuthCode
     *            验证码
     * @param pastdueTime
     *            过期时间
     * @return 操作结果状态值 ，有四种{@link #S1000}成功，{@link #EX2014}SQL更新执行异常，
     *         {@link #ER5001}调用参数非法，其他返回值均为非法值
     */
    StatusCodeBean insertRetrieverPasswordAuthCode(int mUserID, String mEmail,
            String mAuthCode, long pastdueTime);

    /**
     * 查询邮件找回密码步骤中的认证码
     * 
     * @param mUserID
     *            用户ID
     * @param mEmail
     *            邮箱
     * @return 认证码结果，包含状态码。状态值分别为{@link #S1000}查询成功，{@link #R6008}为无参数对应的数据，
     *         {@link #ER5001}参数错误，{@link #EX2016}数据库查询异常。
     */
    AuthCodeStatusCodeBean getRetrieverPasswordAuthCode(int mUserID,
            String mEmail);

    /**
     * 开启事务
     * 
     * @return true为开启成功，false为开启失败
     */
    boolean beginTransaction();

    /**
     * 提交事务
     * 
     * @return true为提交成功，false为提交失败
     */
    boolean commitTransaction();

    /**
     * 关闭事务
     */
    void closeTransaction();

    /**
     * 回滚事务
     */
    void rollbackTransaction();

}
