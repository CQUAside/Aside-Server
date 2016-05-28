package com.round.aside.server.module.dbmanager;

import com.round.aside.server.bean.LoginUserBean;
import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.StatusCodeBean;
import com.round.aside.server.entity.AdvertisementEntity;
import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;

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
    StatusCodeBean insertUser(int mUserID, String mAccount, String mPassword, String mPhone);

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
     * @return 状态码
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
     * @return 登陆用户实体类的建造者
     */
    LoginUserBean loginCheck(String mAccount, String mPassword, long period);

    /**
     * Token令牌登陆检查
     * 
     * @param userId
     *            用户ID
     * @param token
     *            令牌
     * @return 结果状态码，有且仅有以下几种，分别为{@link #S1000}合法，{@link #ER5001}调用参数非法，
     *         {@link #R6006}Token非法，{@link #R6007}Token失效，{@link #EX2016}SQL查询执行异常。
     */
    StatusCodeBean tokenLoginCheck(int userId, String token);

    /**
     * 插入一条图片记录，但只插入PicId字段，以避免PicId重复
     * 
     * @param picId
     *            图片ID
     * @return 状态码机器对应秒速
     */
    StatusCodeBean insertPicWithPicId(String picId);

    /**
     * 更新图片记录的大部分字段，暂不更新广告id。
     * 
     * @param picId
     *            图片ID
     * @param order
     *            序号
     * @param originalPath
     *            原图相对路径
     * @param thumbPath
     *            缩略图相对路径
     * @param extension
     *            文件扩展名
     * @return 结果状态码及其对应描述，有且仅有以下几种，分别为{@link #S1000}成功，{@link #ER5001}调用参数非法，
     */
    StatusCodeBean updatePicWithOutAdId(String picId, int order, String originalPath,
            String thumbPath, String extension);

    /**
     * 新增加一个广告信息记录，插入广告信息的全部字段
     * 
     * @param ad
     *            广告实体
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功即 广告插入成功，{@link #F8001}
     *         id字段冲突（包括adID、Thumbnail_ID、CarrouselID）， {@link #EX2013}SQL插入执行异常
     *         以及{@link #ER5001}参数非法，其他返回值均为非法值。
     */
    int insertAD(AdvertisementEntity ad);

    /**
     * 根据广告ID，查询一条广告信息记录
     * 
     * @param adID
     *            广告ID
     * @return AdAndStatusCode
     *         一个部分广告信息加上返回状态码的一个实体（广告信息包括：广告状态、广告收藏量、广告点击量、用户ID）
     */
    AdAndStatusCode queryAD(int adID);

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
    int updateAD(int adID, AdAndStatusCode ad);

    /**
     * 根据广告ID删除一个广告
     * 
     * @param adID
     *            广告ID
     * @return 操作结果状态值，分别为{@link #S1000}成功即 广告删除成功 {@link #EX2015}SQL删除执行异常 以及
     *         {@link #ER5001}参数非法等
     */
    int deleteAD(int adID);

    /**
     * 插入一个用户收藏广告的记录
     * 
     * @param personalCollection
     *            收藏实体
     * @return操作结果状态值，分别为{@link #S1000}成功即 插入收藏记录成功 {@link #EX2013}SQL插入执行异常 以及
     *                          {@link #ER5001}参数非法等
     */
    int insertCollection(PersonalCollectionEntity personalCollection);

    /**
     * 根据用户ID和广告ID删除一个用户收藏广告的记录
     * 
     * @param adID
     *            广告ID
     * @param userID
     *            用户ID
     * @return操作结果状态值，分别为{@link #S1000}成功即 删除收藏记录成功 {@link #EX2015}SQL删除执行异常 以及
     *                          {@link #ER5001}参数非法等
     */
    int deleteCollecion(int adID, int userID);

    /**
     * 用户举报广告时，插入一个举报广告记录
     * 
     * @param informAd
     *            举报广告实体（包括用户ID，广告ID...）
     * @return操作结果状态值，分别为{@link #S1000}成功即 插入举报记录成功 {@link #EX2013}SQL插入执行异常 以及
     *                          {@link #ER5001}参数非法等
     */
    int insertInformAd(InformAdsEntity informAd);

    /**
     * 用户举报用户时，插入一个举报用户记录
     * 
     * @param informUser
     *            举报用户实体（包括举报人ID，别举报人ID，举报原因....）
     * @return操作结果状态值，分别为{@link #S1000}成功即 插入举报记录成功 {@link #EX2013}SQL插入执行异常 以及
     *                          {@link #ER5001}参数非法等
     */
    int insertInformUser(InformUsersEntity informUser);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @param registerID
     *            用户验证邮箱生成的验证码
     * @return 操作结果状态值 ，有四种{@link #S1000}成功，{@link #EX2014}SQL更新执行异常，
     *         {@link #ER5001}调用参数非法，其他返回值均为非法值
     */
    int updateUserRegister(int mUserID, String registerID);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @param registerID
     *            用户验证邮箱生成的验证码
     * @return
     */
    int selectUserRegister(int mUserID, String registerID);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @return
     */
    int selectUserStatus(int mUserID);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @return
     */
    int updateUserStatus(int mUserID);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @return
     */
    String selectUserEmail(int mUserID);

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
