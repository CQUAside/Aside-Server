package com.round.aside.server.module.dbmanager;

import com.round.aside.server.entity.AdvertisementEntity;
import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;
import java.util.LinkedList;

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
     *         {@link #EX2013}SQL查询执行异常， {@link #ER5001}调用参数非法。
     */
    int checkAccountExistence(String mAccount);

    /**
     * 暂存注册账号步骤中手机号码及对应验证码
     * 
     * @param mPhone
     *            注册手机号
     * @param mAuthcode
     *            对应验证码
     * @return 表征结果的状态码，分别有分别有{@link #S1000}SQL执行正常，{@link #EX2013}SQL插入执行异常，{@link #ER5001}调用参数非法。
     */
    int stashRegisterPhoneAuthcode(String mPhone, String mAuthcode);

    /**
     * 新插入一条用户信息记录，但只插入用户ID，账号和密码三个字段，其余字段使用Update方法进行更新式填充
     * 
     * @param mUserID
     *            用户ID，Unique值，范围为1-99999999
     * @param mAccount
     *            用户账号，Unique值，不能为空
     * @param mPassword
     *            用户密码，不能为空
     * @param NickName
     *            用户昵称，可为空
     * @param Email
     *            用户Email，可为空
     * @param PhoneNum
     *            用户电话，可为空
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功，{@link #F8001}userid字段冲突， {@link #F8002}account字段冲突， {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}调用参数非法，其他返回值均为非法值。
     */
    int insertUser(int mUserID, String mAccount, String mPassword);

    /**
     * 新增加一个广告信息记录，插入广告信息的全部字段
     * 
     * @param ad
     *            广告实体
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功即 广告插入成功，{@link #F8001}id字段冲突（包括adID、Thumbnail_ID、CarrouselID）， {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}参数非法，其他返回值均为非法值。
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
     * @param mAccount
     *            用户账号，Unique值，不能为空
     * @param mPassword
     *            用户密码，不能为空
     * @return 操作结果状态值和用户ID，合法的结果值只有六种，分别为{@link #S1000}和用户ID成功，其他情况ID为0， {@link #EX2016}SQL查询执行异常 以及{@link #ER5001}调用参数非法，其他返回值均为非法值。
     */
    LinkedList<Integer> selectUser(String mAccount, String mPassword);

    /**
     * 
     * @param mUserID
     *            用户ID
     * @param registerID
     *            用户验证邮箱生成的验证码
     * @return 操作结果状态值 ，有四种{@link #S1000}成功，{@link #EX2014}SQL更新执行异常，{@link #ER5001}调用参数非法，其他返回值均为非法值
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
    
}
