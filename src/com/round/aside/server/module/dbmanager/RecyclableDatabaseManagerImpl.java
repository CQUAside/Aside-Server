package com.round.aside.server.module.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.round.aside.server.DB.DataSource;
import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.bean.entity.AdStatusEntity;
import com.round.aside.server.bean.entity.PublishAdEntity;
import com.round.aside.server.bean.statuscode.AdStatusCodeBean;
import com.round.aside.server.bean.statuscode.AuthCodeStatusCodeBean;
import com.round.aside.server.bean.statuscode.EmailStatusCodeBean;
import com.round.aside.server.bean.statuscode.LoginUserBean;
import com.round.aside.server.bean.statuscode.StatusCodeBean;
import com.round.aside.server.bean.statuscode.UserEmailAuthStatusBean;
import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;
import com.round.aside.server.enumeration.AdStatusEnum;
import com.round.aside.server.enumeration.UserEmailStatusEnum;
import com.round.aside.server.module.IModuleFactoryRecycleCallback;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 可回收复用对象的数据库管理模块实现类
 * 
 * @author A Shuai
 * @date 2016-4-30
 * 
 */
public final class RecyclableDatabaseManagerImpl implements IDatabaseManager {

    /**
     * 同步计数器
     */
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final int mID;

    private IModuleFactoryRecycleCallback<IDatabaseManager> mRecycleCallback;

    private Connection mConnection;

    private PreparedStatement mPreState;
    private PreparedStatement mPreTwoState;
    private ResultSet mResultSet;

    public RecyclableDatabaseManagerImpl()
            throws NoAvailableJDBCConnectionException {
        mConnection = DataSource.getConnection();
        if (mConnection == null) {
            throw new NoAvailableJDBCConnectionException(
                    "fail to get a jdbc connection!");
        }

        mID = COUNTER.getAndIncrement();
    }

    private static final String CHECK_ACCOUNT_EXIST_FORMAT = "select userid from aside_user where account = ?";

    @Override
    public StatusCodeBean checkAccountExistence(String mAccount) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmpty(mAccount)) {
            return mBuilder.setStatusCode(ER5001).setMsg("账号参数非法").build();
        }

        boolean mExistence = false;
        try {
            mPreState = mConnection
                    .prepareStatement(CHECK_ACCOUNT_EXIST_FORMAT);
            mPreState.setString(1, mAccount);
            mResultSet = mPreState.executeQuery();

            while (mResultSet.next()) {
                mExistence = true;
                break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return mBuilder.setStatusCode(EX2016).setMsg("数据库查询异常，请重试").build();
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }

        if (mExistence) {
            mBuilder.setStatusCode(R6002).setMsg("账号重复");
        } else {
            mBuilder.setStatusCode(S1000).setMsg("账号检查通过");
        }
        return mBuilder.build();
    }

    private static final String DELETE_PHONEAUTH_FORMAT = "delete from aside_authcode where phone = ?";
    private static final String INSERT_PHONEAUTH_FORMAT = "insert into aside_authcode(phone, authcode, pastdue_time) values(?, ?, ?)";

    @Override
    public StatusCodeBean stashRegisterPhoneAuthcode(String mPhone,
            String mAuthcode, long pastdueTime) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmpty(mPhone) || StringUtil.isEmpty(mAuthcode)
                || mAuthcode.length() != 4) {
            return mBuilder.setStatusCode(ER5001).setMsg("账号参数非法").build();
        }

        try {
            mPreState = mConnection.prepareStatement(DELETE_PHONEAUTH_FORMAT);
            mPreState.setString(1, mPhone);
            mPreState.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeMemberPreparedStatement();
        }

        try {
            mPreState = mConnection.prepareStatement(INSERT_PHONEAUTH_FORMAT);
            mPreState.setString(1, mPhone);
            mPreState.setString(2, mAuthcode);
            mPreState.setLong(3, pastdueTime);
            mPreState.executeUpdate();
            mBuilder.setStatusCode(S1000).setMsg("暂存手机验证码成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2013).setMsg("数据库插入异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }

        return mBuilder.build();
    }

    private static final String SELECT_PHONEAUTH_FORMAT = "select id, pastdue_time  from aside_authcode where phone = ? and authcode = ? order by id desc";

    @Override
    public StatusCodeBean checkRegisterPhoneAuthcode(String mPhone,
            String mAuthcode, long currentTime) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmpty(mPhone) || StringUtil.isEmpty(mAuthcode)
                || mAuthcode.length() != 4) {
            return mBuilder.setStatusCode(ER5001).setMsg("参数非法").build();
        }

        long pastdueTime = 0;

        try {
            mPreState = mConnection.prepareStatement(SELECT_PHONEAUTH_FORMAT);
            mPreState.setString(1, mPhone);
            mPreState.setString(2, mAuthcode);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                pastdueTime = mResultSet.getLong(2);
                break;
            }

            if (pastdueTime == 0) {
                mBuilder.setStatusCode(ER5003L).setMsg("验证码不符");
            } else if (currentTime > pastdueTime) {
                mBuilder.setStatusCode(ER5004L).setMsg("验证码过期，请重新获取");
            } else {
                mBuilder.setStatusCode(S1000).setMsg("手机验证码验证通过");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return mBuilder.setStatusCode(EX2016).setMsg("数据库查询异常").build();
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
        return mBuilder.build();
    }

    private static final String INSERT_USER_FORMAT = "INSERT into aside_user(userid, account, password, phonenum) values(?, ?, ?, ?)";

    @Override
    public StatusCodeBean insertUser(int mUserID, String mAccount,
            String mPassword, String mPhone) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (mUserID <= 0 || StringUtil.isEmpty(mAccount)
                || StringUtil.isEmpty(mPassword)) {
            return mBuilder.setStatusCode(ER5001).setMsg("参数非法").build();
        }

        try {
            mPreState = mConnection.prepareStatement(INSERT_USER_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mAccount);
            mPreState.setString(3, mPassword);
            mPreState.setString(4, mPhone);
            mPreState.executeUpdate();

            mBuilder.setStatusCode(S1000).setMsg("用户账号插入成功");
        } catch (SQLIntegrityConstraintViolationException e) {
            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("userid'") != -1) {
                mBuilder.setStatusCode(F8001).setMsg("UserId冲突，请重新生成再次插入");
            } else if (mExMsg.indexOf("account") != -1) {
                mBuilder.setStatusCode(F8002).setMsg("Account账号冲突");
            } else {
                e.printStackTrace();
                throw new IllegalStateException(
                        "This is a improper exception, please check your code!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2013).setMsg("数据库插入异常");
        } finally {
            closeMemberPreparedStatement();
        }

        return mBuilder.build();
    }

    private static final String INSERT_TOKEN_FORMAT = "INSERT into aside_token(userid, os, browser, detail_browser, token, login_time, pastdue_time) values(?, ?, ?, ?, ?, ?, ?)";

    @Override
    public StatusCodeBean insertToken(int mUserID,
            RequestInfoBean mRequestInfoBean, String mToken, long loginTime,
            long pastdueTime) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (mUserID <= 0 || StringUtil.isEmptyInSet(mToken)) {
            return mBuilder.setStatusCode(ER5001).setMsg("参数非法").build();
        }
        try {
            mPreState = mConnection.prepareStatement(INSERT_TOKEN_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mRequestInfoBean.getOS());
            mPreState.setString(3, mRequestInfoBean.getBrowserName());
            mPreState.setString(4, mRequestInfoBean.getBrowNameAndVer());
            mPreState.setString(5, mToken);
            mPreState.setLong(6, loginTime);
            mPreState.setLong(7, pastdueTime);
            mPreState.executeUpdate();

            mBuilder.setStatusCode(S1000).setMsg("Token插入成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2013).setMsg("数据库插入异常");
        } finally {
            closeMemberPreparedStatement();
        }
        return mBuilder.build();
    }

    private static final String SELECT_USERID_FORMAT = "select userid, password from aside_user where account = ?";

    @Override
    public LoginUserBean loginCheck(String mAccount, String mPassword,
            long period) {
        LoginUserBean.Builder mBuilder = new LoginUserBean.Builder();

        if (StringUtil.isEmptyInSet(mAccount, mPassword) || period <= 0) {
            mBuilder.setStatusCode(ER5001).setMsg("账号或密码参数非法，请检查后重新输入");
            return mBuilder.build();
        }

        String mTPassword = null;
        try {
            mPreState = mConnection.prepareStatement(SELECT_USERID_FORMAT);
            mPreState.setString(1, mAccount);
            mResultSet = mPreState.executeQuery();

            while (mResultSet.next()) {
                mTPassword = mResultSet.getString(2);
                break;
            }

            if (mTPassword == null) {
                mBuilder.setStatusCode(R6004).setMsg("账号不存在");
            } else if (!mTPassword.equals(mPassword)) {
                mBuilder.setStatusCode(R6005).setMsg("密码输入错误，请重新输入");
            } else {
                mBuilder.setStatusCode(S1000).setMsg("账号密码验证通过");
                mBuilder.setUserID(mResultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2016).setMsg("数据库查询异常，请重试");
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }

        return mBuilder.build();
    }

    private static final String SELECT_TOKEN_FORMAT = "select pastdue_time from aside_token where userid = ? and token = ?";

    @Override
    public StatusCodeBean tokenLoginCheck(int userId, String token) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (userId <= 0 || StringUtil.isEmptyInSet(token)) {
            return mBuilder.setStatusCode(ER5001).setMsg("参数非法").build();
        }

        try {
            mPreState = mConnection.prepareStatement(SELECT_TOKEN_FORMAT);
            mPreState.setInt(1, userId);
            mPreState.setString(2, token);
            mResultSet = mPreState.executeQuery();

            long mPastdueTime = 0;
            while (mResultSet.next()) {
                mPastdueTime = mResultSet.getLong(1);
                break;
            }

            if (mPastdueTime == 0) {
                mBuilder.setStatusCode(R6006).setMsg("Token非法，请登录");
            } else if (mPastdueTime >= System.currentTimeMillis()) {
                mBuilder.setStatusCode(R6007).setMsg("Token过期，请重新登录");
            } else {
                mBuilder.setStatusCode(S1000).setMsg("Token验证成功");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2016).setMsg("数据库查询异常");
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }

        return mBuilder.build();
    }

    private static final String INSERT_PIC_FORMAT = "INSERT into aside_pic(picid) values(?)";

    @Override
    public StatusCodeBean insertPicWithPicId(String picId) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmpty(picId)) {
            return mBuilder.setStatusCode(ER5001).setMsg("图片Id非法").build();
        }

        try {
            mPreState = mConnection.prepareStatement(INSERT_PIC_FORMAT);
            mPreState.setString(1, picId);
            mPreState.executeUpdate();

            mBuilder.setStatusCode(S1000).setMsg("Pic插入成功");
        } catch (SQLIntegrityConstraintViolationException e) {
            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("picid'") != -1) {
                mBuilder.setStatusCode(EX2017).setMsg("图片id重复");
            } else {
                e.printStackTrace();
                throw new IllegalStateException(
                        "This is a improper exception, please check your code!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2013).setMsg("数据库插入异常");
        } finally {
            closeMemberPreparedStatement();
        }
        return mBuilder.build();
    }

    private static final String UPDATE_PIC_FORMAT = "UPDATE aside_pic SET order = ?, originalpath = ?, thumbpath = ?, extension = ? WHERE picid = ?";

    @Override
    public StatusCodeBean updatePicWithOutAdId(String picId, int order,
            String originalPath, String thumbPath, String extension) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmptyInSet(picId, originalPath, thumbPath)) {
            return mBuilder.setStatusCode(ER5001)
                    .setMsg("参数非法，图片id、原图相对路径以及缩略图相对路径不可为空").build();
        }
        if (order < 0) {
            return mBuilder.setStatusCode(ER5001).setMsg("参数非法，序号需为自然数")
                    .build();
        }

        try {
            mPreState = mConnection.prepareStatement(UPDATE_PIC_FORMAT);

            mPreState.setInt(1, order);
            mPreState.setString(2, originalPath);
            mPreState.setString(3, thumbPath);
            mPreState.setString(4, extension);
            mPreState.setString(5, picId);
            mPreState.executeUpdate();

            mBuilder.setStatusCode(S1000).setMsg("Pic信息更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2014).setMsg("数据库更新异常");
        } finally {
            closeMemberPreparedStatement();
        }

        return mBuilder.build();
    }

    public static final String uploadingAdsql = "INSERT into aside_advertisement(AdID, Thumbnail_ID, CarrouselID, Title, Content, StartTime, Deadline, Money, Status, ClickCount, CollectCount, UserID) values(?, ?, ?,?,?,?,?,?,?,?,?,?)";
    public static final String INSERT_AD = "INSERT into aside_advertisement(Thumbnail_ID, Title, Content, StartTime, Deadline, Status, UserID) values(?, ?, ?, ?, ?, ?, ?)";
    public static final String QUERY_ADID = "SELECT AdID from aside_advertisement where UserID = ? and Thumbnail_ID = ? ORDER BY AdID DESC";
    public static final String INSERT_ADAREA = "INSERT into aside_adarea(adId, areaId) values(?, ?)";

    @Override
    public StatusCodeBean insertAD(PublishAdEntity ad, int mUserID,
            AdStatusEnum mAdStatusEnum) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (StringUtil.isEmpty(ad.getAdLogoImgID()))
            return mBuilder.setStatusCode(ER5001).setMsg("缩略图ID非法").build();

        if (mUserID <= 0)
            return mBuilder.setStatusCode(ER5001).setMsg("UserID非法").build();

        if (StringUtil.isEmptyInSet(ad.getAdTitle(), ad.getAdDescription()))
            return mBuilder.setStatusCode(ER5001).setMsg("广告参数非法").build();

        if (!ad.getAdEndTimestamp().after(new Date())) {
            return mBuilder.setStatusCode(ER5001).setMsg("广告结束时间非法").build();
        }

        try {
            mConnection.setAutoCommit(false);

            mPreState = mConnection.prepareStatement(INSERT_AD);
            mPreState.setString(1, ad.getAdLogoImgID());
            mPreState.setString(2, ad.getAdTitle());
            mPreState.setString(3, ad.getAdDescription());
            mPreState.setTimestamp(4, ad.getAdStartTimestamp());
            mPreState.setTimestamp(5, ad.getAdEndTimestamp());
            mPreState.setInt(6, mAdStatusEnum.getType());
            mPreState.setInt(7, mUserID);
            mPreState.executeUpdate();
            closeMemberPreparedStatement();

            int mAdID = -1;

            mPreState = mConnection.prepareStatement(QUERY_ADID);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, ad.getAdLogoImgID());
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                mAdID = mResultSet.getInt(1);
                break;
            }

            if (mAdID != -1) {
                mBuilder.setStatusCode(EX2013).setMsg("数据库插入异常，请重试");
            }

            mPreTwoState = mConnection.prepareStatement(INSERT_ADAREA);
            List<String> mAreaSet = ad.getAdAreaSet();
            for (String str : mAreaSet) {
                mPreTwoState.setInt(1, mAdID);
                mPreTwoState.setString(2, str);
                mPreTwoState.addBatch();
            }
            mPreTwoState.executeBatch();

            mConnection.commit();

            mBuilder.setStatusCode(S1000).setMsg("广告插入成功");
        } catch (SQLException e) {
            rollbackTransaction();
            e.printStackTrace();
            mBuilder.setStatusCode(EX2013).setMsg("数据库插入异常，请重试");
        } finally {
            closeTransaction();
            closeMemberResultSet();
            closeMemberPreparedStatement();
            closeMemberPreparedTwoStatement();
        }
        return mBuilder.build();
    }

    // 审核广告时查询广告信息用的SQL语句
    public static final String queryAdsql = "select ad.Status, ad.UserID, ad.CollectCount, ad.ClickCount from aside_advertisement ad where ad.AdID = ?";

    @Override
    public AdStatusCodeBean queryAD(int adID) {
        AdStatusCodeBean.Builder mBuilder = new AdStatusCodeBean.Builder();
        if (adID <= 0 || adID > 99999999) {
            mBuilder.setStatusCode(ER5001).setMsg("广告ID非法");
            return mBuilder.build();
        }

        try {
            boolean find = false;

            mPreState = mConnection.prepareStatement(queryAdsql);
            mPreState.setInt(1, adID);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                mBuilder.setAdStatusType(mResultSet.getInt("Status"));
                mBuilder.setClickCount(mResultSet.getInt("ClickCount"));
                mBuilder.setCollectCount(mResultSet.getInt("CollectCount"));
                mBuilder.setUserID(mResultSet.getInt("UserID"));
                mBuilder.setStatusCode(S1000).setMsg("广告数据查询成功");
                find = true;
                break;
            }

            if (!find) {
                mBuilder.setStatusCode(R6008).setMsg("未查询到广告数据，即AdID无效");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2016).setMsg("数据库查询异常");
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
        return mBuilder.build();
    }

    // 更新广告时用的SQL语句，包括更新状态、点击量、收藏量。
    public static final String updateAdsql = "update aside_advertisement set Status = ?, ClickCount = ?, CollectCount = ? where AdID = ?";

    @Override
    public StatusCodeBean updateAD(int adID, AdStatusEntity ad) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (adID <= 0 || adID > 99999999)
            return mBuilder.setStatusCode(ER5001).setMsg("AdID参数非法").build();

        try {
            mPreState = mConnection.prepareStatement(updateAdsql);
            mPreState.setInt(1, ad.getAdStatus().getType());
            mPreState.setInt(2, ad.getClickCount());
            mPreState.setInt(3, ad.getCollectCount());
            mPreState.setInt(4, adID);

            mPreState.executeUpdate();

            mBuilder.setStatusCode(S1000).setMsg("广告状态更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2014).setMsg("数据库更新异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }
        return mBuilder.build();
    }

    // 删除广告时用的SQL语句
    public static final String deleteAdsql = "delete from aside_advertisement where AdID = ?";

    @Override
    public StatusCodeBean deleteAD(int adID) {
        StatusCodeBean.Builder mBuilder = new StatusCodeBean.Builder();

        if (adID <= 0 || adID > 99999999)
            return mBuilder.setStatusCode(ER5001).setMsg("AdID参数非法").build();

        try {
            mPreState = mConnection.prepareStatement(deleteAdsql);
            mPreState.setInt(1, adID);
            mPreState.executeUpdate();

            mBuilder.setStatusCode(S1000).setMsg("广告删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mBuilder.setStatusCode(EX2015).setMsg("数据库删除异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }
        return mBuilder.build();
    }

    // 用户收藏广告时插入一个收藏记录
    public static final String insertCollectionsql = "insert into aside_collection(UserID, AdID) values(?, ?)";

    @Override
    public StatusCodeBean insertCollection(
            PersonalCollectionEntity personalCollection) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (personalCollection.getAdID() <= 0
                || personalCollection.getAdID() > 99999999)
            return mResultBuilder.setStatusCode(ER5001).setMsg("AdID参数非法")
                    .build();
        if (personalCollection.getUserID() <= 0
                || personalCollection.getUserID() > 99999999) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }

        try {
            mPreState = mConnection.prepareStatement(insertCollectionsql);
            mPreState.setInt(1, personalCollection.getUserID());
            mPreState.setInt(2, personalCollection.getAdID());
            mPreState.executeUpdate();

            mResultBuilder.setStatusCode(S1000).setMsg("收藏成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2013).setMsg("数据库插入异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }
        return mResultBuilder.build();
    }

    // 用户取消收藏时，删除一个收藏记录
    public static final String deleteCollectionsql = "delete from aside_collection where AdID = ? and UserID = ?";

    @Override
    public StatusCodeBean deleteCollecion(int adID, int userID) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (adID <= 0 || adID > 99999999)
            return mResultBuilder.setStatusCode(ER5001).setMsg("AdID参数非法")
                    .build();
        if (userID <= 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }

        try {
            mPreState = mConnection.prepareStatement(deleteCollectionsql);
            mPreState.setInt(1, adID);
            mPreState.setInt(2, userID);
            mPreState.executeUpdate();

            mResultBuilder.setStatusCode(S1000).setMsg("取消收藏成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2015).setMsg("数据库删除异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }
        return mResultBuilder.build();
    }

    // 用户举报广告时，向举报广告表插入一个举报记录
    public static final String insertInformAdsql = "insert into aside_informAds(UserID, AdID) values(?, ?)";

    @Override
    public StatusCodeBean insertInformAd(InformAdsEntity informAd) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (informAd.getAdID() <= 0 || informAd.getAdID() > 99999999)
            return mResultBuilder.setStatusCode(ER5001).setMsg("被举报AdID参数非法")
                    .build();
        if (informAd.getUserID() <= 0)
            return mResultBuilder.setStatusCode(ER5001).setMsg("举报人UserID参数非法")
                    .build();

        try {
            mPreState = mConnection.prepareStatement(insertInformAdsql);
            mPreState.setInt(1, informAd.getUserID());
            mPreState.setInt(2, informAd.getAdID());
            mPreState.executeUpdate();

            mResultBuilder.setStatusCode(S1000).setMsg("举报广告成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2013).setMsg("数据库插入异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }
        return mResultBuilder.build();
    }

    // 用户举报别的用户时，向举报用户表插入一个举报记录
    public static final String insertInformUsersql = "insert into aside_informUser(UserID, InformedUserID, InformReason) values(?, ?, ?)";

    @Override
    public StatusCodeBean insertInformUser(InformUsersEntity informUser) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (informUser.getUserID() <= 0)
            return mResultBuilder.setStatusCode(ER5001).setMsg("举报人UserID参数非法")
                    .build();
        if (informUser.getInformedUserID() <= 0)
            return mResultBuilder.setStatusCode(ER5001)
                    .setMsg("被举报人UserID参数非法").build();

        try {
            mPreState = mConnection.prepareStatement(insertInformUsersql);
            mPreState.setInt(1, informUser.getUserID());
            mPreState.setInt(2, informUser.getInformedUserID());
            mPreState.setString(3, informUser.getInformReason());
            mPreState.executeUpdate();

            mResultBuilder.setStatusCode(S1000).setMsg("举报用户成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2013).setMsg("数据库插入异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }

        return mResultBuilder.build();
    }

    private static final String DELETE_EMAIL_AUTHCODE = "delete from aside_authcode where userid = ? and email = ?";
    private static final String INSERT_EMAIL_AUTHCODE = "insert into aside_authcode(userid, email, authcode, pastdue_time) values(?, ?, ?, ?)";

    @Override
    public StatusCodeBean insertUserEmailAuthCode(int mUserID, String mEmail,
            String mAuthCode, long pastdueTime) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (mUserID <= 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }
        if (mAuthCode.length() != 4) {
            return mResultBuilder.setStatusCode(ER5001)
                    .setMsg("AuthCode参数长度必须为四").build();
        }

        try {
            mPreState = mConnection.prepareStatement(DELETE_EMAIL_AUTHCODE);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mEmail);
            mPreState.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeMemberPreparedStatement();
        }

        try {
            mPreState = mConnection.prepareStatement(INSERT_EMAIL_AUTHCODE);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mEmail);
            mPreState.setString(3, mAuthCode);
            mPreState.setLong(4, pastdueTime);
            mPreState.executeUpdate();

            mResultBuilder.setStatusCode(S1000).setMsg("邮件认证码写入成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2013).setMsg("数据库插入异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }

        return mResultBuilder.build();
    }

    private static final String SELECT_EMAIL_AUTHCODE = "select authcode from aside_authcode where userid = ? and email = ?";

    @Override
    public AuthCodeStatusCodeBean getUserEmailAuthCode(int mUserID,
            String mEmail) {
        AuthCodeStatusCodeBean.Builder mResultBuilder = new AuthCodeStatusCodeBean.Builder();
        if (mUserID <= 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法");
            return mResultBuilder.build();
        }

        try {
            boolean find = false;

            mPreState = mConnection.prepareStatement(SELECT_EMAIL_AUTHCODE);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mEmail);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                mResultBuilder.setAuthCode(mResultSet.getString(1));
                find = true;
                break;
            }

            if (find) {
                mResultBuilder.setStatusCode(S1000).setMsg("查询成功");
            } else {
                mResultBuilder.setStatusCode(R6008).setMsg("无对应的邮箱认证码");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2016).setMsg("数据库查询异常，请重试");
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }

        return mResultBuilder.build();
    }

    private static final String SELECT_USER_STATUS = "SELECT status from aside_user where userid = ?";

    @Override
    public UserEmailAuthStatusBean selectUserEmailStatus(int mUserID) {
        UserEmailAuthStatusBean.Builder mResultBuilder = new UserEmailAuthStatusBean.Builder();

        if (mUserID <= 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法");
            return mResultBuilder.build();
        }

        mResultBuilder.setUserEmailStatusType(UserEmailStatusEnum.UNAUTH
                .getType());
        try {
            boolean find = false;

            mPreState = mConnection.prepareStatement(SELECT_USER_STATUS);
            mPreState.setInt(1, mUserID);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                mResultBuilder.setUserEmailStatusType(mResultSet
                        .getInt("status"));
                find = true;
                break;
            }

            if (find) {
                mResultBuilder.setStatusCode(S1000).setMsg("查询成功");
            } else {
                mResultBuilder.setStatusCode(R6008).setMsg("UserID错误，无此数据");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2016).setMsg("数据库查询异常，请重试");
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }

        return mResultBuilder.build();
    }

    private static final String UPDATE_USER_STATUS = "UPDATE aside_user set status = ? where userid = ?";

    @Override
    public StatusCodeBean updateUserEmailStatus(int mUserID,
            UserEmailStatusEnum mEmailStatus) {
        StatusCodeBean.Builder mResultBuilder = new StatusCodeBean.Builder();
        if (mUserID <= 0) {
            return mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法")
                    .build();
        }
        if (mEmailStatus == UserEmailStatusEnum.UNAUTH) {
            return mResultBuilder.setStatusCode(ER5001)
                    .setMsg("EmailStatus邮箱认证状态参数非法").build();
        }

        try {
            mPreState = mConnection.prepareStatement(UPDATE_USER_STATUS);
            mPreState.setInt(1, mEmailStatus.getType());
            mPreState.setInt(2, mUserID);
            mPreState.executeUpdate();

            mResultBuilder.setStatusCode(S1000).setMsg("修改邮箱认证状态成功");
        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2014).setMsg("数据库更新异常，请重试");
        } finally {
            closeMemberPreparedStatement();
        }
        return mResultBuilder.build();
    }

    private static final String SELECT_USER_EMAIL = "SELECT　email from aside_user where userid=?";

    @Override
    public EmailStatusCodeBean selectUserEmail(int mUserID) {
        EmailStatusCodeBean.Builder mResultBuilder = new EmailStatusCodeBean.Builder();

        if (mUserID <= 0) {
            mResultBuilder.setStatusCode(ER5001).setMsg("UserID参数非法");
            return mResultBuilder.build();
        }

        try {
            boolean find = false;

            mPreState = mConnection.prepareStatement(SELECT_USER_EMAIL);
            mPreState.setInt(1, mUserID);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                mResultBuilder.setUserEmail(mResultSet.getString("email"));
                find = true;
            }

            if (find) {
                mResultBuilder.setStatusCode(S1000).setMsg("UserID对应的用户邮箱查询成功");
            } else {
                mResultBuilder.setStatusCode(R6008).setMsg("无此UserID对应的用户数据");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mResultBuilder.setStatusCode(EX2016).setMsg("数据库查询异常，请重试");
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
        return mResultBuilder.build();
    }

    @Override
    public StatusCodeBean insertRetrieverPasswordAuthCode(int mUserID,
            String mEmail, String mAuthCode, long pastdueTime) {
        return insertUserEmailAuthCode(mUserID, mEmail, mAuthCode, pastdueTime);
    }

    @Override
    public AuthCodeStatusCodeBean getRetrieverPasswordAuthCode(int mUserID,
            String mEmail) {
        return getUserEmailAuthCode(mUserID, mEmail);
    }

    @Override
    public void release() {
        mRecycleCallback.onRecycleModule(this);
    }

    @Override
    public void registerModuleFactoryRecycle(
            IModuleFactoryRecycleCallback<IDatabaseManager> mRecycle) {
        mRecycleCallback = mRecycle;
    }

    @Override
    public boolean onReuse() {
        try {
            if (mConnection.isClosed() || !mConnection.isValid(1)) {
                mConnection = DataSource.getConnection();
                if (mConnection == null) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 关闭PreparedStatement的成员变量
     */
    private void closeMemberPreparedStatement() {
        if (mPreState != null) {
            try {
                mPreState.close();
            } catch (Exception e1) {
            } finally {
                mPreState = null;
            }
        }
    }

    private void closeMemberPreparedTwoStatement() {
        if (mPreTwoState != null) {
            try {
                mPreTwoState.close();
            } catch (Exception e1) {
            } finally {
                mPreTwoState = null;
            }
        }
    }

    /**
     * 关闭ResultSet的成员变量
     */
    private void closeMemberResultSet() {
        if (mResultSet != null) {
            try {
                mResultSet.close();
            } catch (Exception e1) {
            } finally {
                mResultSet = null;
            }
        }
    }

    @Override
    public int hashCode() {
        return mID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecyclableDatabaseManagerImpl) {
            RecyclableDatabaseManagerImpl mDBMObj = (RecyclableDatabaseManagerImpl) obj;
            if (mDBMObj.mID == mID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean beginTransaction() {
        try {
            mConnection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean commitTransaction() {
        try {
            mConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void closeTransaction() {
        try {
            mConnection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            mConnection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
