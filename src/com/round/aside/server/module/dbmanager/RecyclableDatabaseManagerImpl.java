package com.round.aside.server.module.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import com.round.aside.server.DB.DataSource;
import com.round.aside.server.bean.RequestInfoBean;
import com.round.aside.server.entity.AdvertisementEntity;
import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;
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

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final int mID;

    private IModuleFactoryRecycleCallback<IDatabaseManager> mRecycleCallback;

    private Connection mConnection;

    private PreparedStatement mPreState;
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
    public int checkAccountExistence(String mAccount) {
        if (StringUtil.isEmpty(mAccount)) {
            return ER5001;
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
            return EX2013;
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }

        return mExistence ? R6002 : S1000;
    }

    private static final String DELETE_PHONEAUTH_FORMAT = "delete from aside_authcode where phone = ?";
    private static final String INSERT_PHONEAUTH_FORMAT = "insert into aside_authcode(phone, authcode, pastdue_time) values(?, ?, ?)";

    @Override
    public int stashRegisterPhoneAuthcode(String mPhone, String mAuthcode,
            long pastdueTime) {
        if (StringUtil.isEmpty(mPhone) || StringUtil.isEmpty(mAuthcode)
                || mAuthcode.length() != 4) {
            return ER5001;
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
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            closeMemberPreparedStatement();
        }

        return S1000;
    }

    private static final String SELECT_PHONEAUTH_FORMAT = "select id, pastdue_time  from aside_authcode where phone = ? and authcode = ? order by id desc";

    @Override
    public int checkRegisterPhoneAuthcode(String mPhone, String mAuthcode,
            long currentTime) {
        if (StringUtil.isEmpty(mPhone) || StringUtil.isEmpty(mAuthcode)
                || mAuthcode.length() != 4) {
            return ER5001;
        }

        long pastdueTime = 0;
        int resultCode;
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
                resultCode = ER5003L;
            } else if (currentTime > pastdueTime) {
                resultCode = ER5004L;
            } else {
                resultCode = S1000;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return EX2016;
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
        return resultCode;
    }

    private static final String INSERT_USER_FORMAT = "INSERT into aside_user(userid, account, password, phonenum) values(?, ?, ?, ?)";

    @Override
    public int insertUser(int mUserID, String mAccount, String mPassword,
            String mPhone) {
        if (mUserID <= 0 || StringUtil.isEmpty(mAccount)
                || StringUtil.isEmpty(mPassword)) {
            return ER5001;
        }

        try {
            mPreState = mConnection.prepareStatement(INSERT_USER_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mAccount);
            mPreState.setString(3, mPassword);
            mPreState.setString(4, mPhone);
            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("userid'") != -1) {
                return F8001;
            } else if (mExMsg.indexOf("account") != -1) {
                return F8002;
            } else {
                e.printStackTrace();
                throw new IllegalStateException(
                        "This is a improper exception, please check your code!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            closeMemberPreparedStatement();
        }

        return S1000;
    }

    private static final String INSERT_TOKEN_FORMAT = "INSERT into aside_token(userid, os, browser, detail_browser, token, login_time, pastdue_time) values(?, ?, ?, ?, ?, ?, ?)";

    @Override
    public int insertToken(int mUserID, RequestInfoBean mRequestInfoBean,
            String mToken, long loginTime, long pastdueTime) {
        if (mUserID <= 0 || StringUtil.isEmptyInSet(mToken)) {
            return ER5001;
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
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    public static final String uploadingAdsql = "INSERT into aside_advertisement(AdID, Thumbnail_ID, CarrouselID, Title, Content, StartTime, Deadline, Money, Status, ClickCount, CollectCount, UserID) values(?, ?, ?,?,?,?,?,?,?,?,?,?)";

    @Override
    public int insertAD(AdvertisementEntity ad) {
        if (ad.getAdID() <= 0 || ad.getAdID() > 99999999)
            return ER5001;

        if (ad.getCarrouselID() <= 0 || ad.getCarrouselID() > 99999999)
            return ER5001;

        if (ad.getThumbnail_ID() <= 0 || ad.getThumbnail_ID() > 99999999)
            return ER5001;

        if (ad.getUserID() <= 0)
            return ER5001;

        if (ad.getTitle().equals("") || ad.getContent().equals("")
                || ad.getStartTime().equals("") || ad.getDeadline().equals(""))
            return ER5001;

        try {
            mPreState = mConnection.prepareStatement(uploadingAdsql);
            mPreState.setInt(1, ad.getAdID());
            mPreState.setInt(2, ad.getThumbnail_ID());
            mPreState.setInt(3, ad.getCarrouselID());
            mPreState.setString(4, ad.getTitle());
            mPreState.setString(5, ad.getContent());
            mPreState.setString(6, ad.getStartTime());
            mPreState.setString(7, ad.getDeadline());
            mPreState.setDouble(8, ad.getMoney());
            mPreState.setInt(9, ad.getStatus());
            mPreState.setInt(10, ad.getClickCount());
            mPreState.setInt(11, ad.getCollectCount());
            mPreState.setInt(12, ad.getUserID());
            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("AdID") != -1
                    || mExMsg.indexOf("Thumbnail_ID") != -1
                    || mExMsg.indexOf("CarrouselID") != -1)
                return F8001;
            else {
                e.printStackTrace();
                throw new IllegalStateException(
                        "This is a improper exception, please check your code!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    // 审核广告时查询广告信息用的SQL语句
    public static final String queryAdsql = "select ad.Status, ad.UserID, ad.CollectCount, ad.ClickCount from aside_advertisement ad where ad.AdID = ?";

    @Override
    public AdAndStatusCode queryAD(int adID) {
        AdAndStatusCode ad = new AdAndStatusCode();
        if (adID <= 0 || adID > 99999999)
            ad.setStatusCode(ER5001);

        try {
            mPreState = mConnection.prepareStatement(queryAdsql);
            mPreState.setInt(1, adID);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                ad.setStatus(mResultSet.getInt("Status"));
                ad.setClickCount(mResultSet.getInt("ClickCount"));
                ad.setCollectCount(mResultSet.getInt("CollectCount"));
                ad.setUserID(mResultSet.getInt("UserID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ad.setStatusCode(EX2016);
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
        return ad;
    }

    // 更新广告时用的SQL语句，包括更新状态、点击量、收藏量。
    public static final String updateAdsql = "update aside_advertisement set Status = ?, ClickCount = ?, CollectCount = ? where AdID = ?";

    @Override
    public int updateAD(int adID, AdAndStatusCode ad) {
        if (adID <= 0 || adID > 99999999)
            ad.setStatusCode(ER5001);

        try {
            mPreState = mConnection.prepareStatement(updateAdsql);
            mPreState.setInt(1, ad.getStatus());
            mPreState.setInt(2, ad.getClickCount());
            mPreState.setInt(3, ad.getCollectCount());
            mPreState.setInt(4, adID);

            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    // 删除广告时用的SQL语句
    public static final String deleteAdsql = "delete from aside_advertisement where AdID = ?";

    @Override
    public int deleteAD(int adID) {
        if (adID <= 0 || adID > 99999999)
            return ER5001;

        try {
            mPreState = mConnection.prepareStatement(deleteAdsql);
            mPreState.setInt(1, adID);
            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2015;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    // 用户收藏广告时插入一个收藏记录
    public static final String insertCollectionsql = "insert into aside_collection(UserID, AdID) values(?, ?)";

    @Override
    public int insertCollection(PersonalCollectionEntity personalCollection) {
        if (personalCollection.getAdID() <= 0
                || personalCollection.getAdID() > 99999999)
            return ER5001;
        if (personalCollection.getUserID() <= 0
                || personalCollection.getUserID() > 99999999) {
            return ER5001;
        }

        try {
            mPreState = mConnection.prepareStatement(insertCollectionsql);
            mPreState.setInt(1, personalCollection.getUserID());
            mPreState.setInt(2, personalCollection.getAdID());
            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("adID") != -1 || mExMsg.indexOf("userID") != -1)
                return F8001;
            else {
                e.printStackTrace();
                throw new IllegalStateException(
                        "This is a improper exception, please check your code!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    // 用户取消收藏时，删除一个收藏记录
    public static final String deleteCollectionsql = "delete from aside_collection where AdID = ? and UserID = ?";

    @Override
    public int deleteCollecion(int adID, int userID) {
        if (adID <= 0 || adID > 99999999)
            return ER5001;
        if (userID <= 0 || userID > 99999999) {
            return ER5001;
        }

        try {
            mPreState = mConnection.prepareStatement(deleteCollectionsql);
            mPreState.setInt(1, adID);
            mPreState.setInt(2, userID);
            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2015;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    // 用户举报广告时，向举报广告表插入一个举报记录
    public static final String insertInformAdsql = "insert into aside_informAds(UserID, AdID) values(?, ?)";

    @Override
    public int insertInformAd(InformAdsEntity informAd) {
        if (informAd.getAdID() <= 0 || informAd.getAdID() > 99999999)
            return ER5001;
        if (informAd.getUserID() <= 0)
            return ER5001;

        try {
            mPreState = mConnection.prepareStatement(insertInformAdsql);
            mPreState.setInt(1, informAd.getUserID());
            mPreState.setInt(2, informAd.getAdID());

            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("AdID") != -1 || mExMsg.indexOf("UserID") != -1)
                return F8001;
            else {
                e.printStackTrace();
                throw new IllegalStateException(
                        "This is a improper exception, please check your code!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    // 用户举报别的用户时，向举报用户表插入一个举报记录
    public static final String insertInformUsersql = "insert into aside_informUser(UserID, InformedUserID, InformReason) values(?, ?, ?)";

    @Override
    public int insertInformUser(InformUsersEntity informUser) {
        if (informUser.getUserID() <= 0)
            return ER5001;
        if (informUser.getInformedUserID() <= 0)
            return ER5001;

        try {
            mPreState = mConnection.prepareStatement(insertInformUsersql);
            mPreState.setInt(1, informUser.getUserID());
            mPreState.setInt(2, informUser.getInformedUserID());
            mPreState.setString(3, informUser.getInformReason());

            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {

            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("UserID") != -1
                    || mExMsg.indexOf("InformedUserID") != -1)
                return F8001;
            else {
                e.printStackTrace();
                throw new IllegalStateException(
                        "This is a improper exception, please check your code!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            closeMemberPreparedStatement();
        }

        return S1000;
    }

    private static final String SELECT_USER_FORMAT = "SELECT userid from aside_user where account=? and password=?";

    @Override
    public LinkedList<Integer> selectUser(String mAccount, String mPassword) {
        LinkedList<Integer> stateUserID = new LinkedList<Integer>();
        int mUserID = 0;
        stateUserID.add(mUserID);
        if (StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)) {
            stateUserID.add(ER5001);
            return stateUserID;
        }

        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_FORMAT);
            mPreState.setString(1, mAccount);
            mPreState.setString(2, mPassword);
            mResultSet = mPreState.executeQuery();
            boolean dataIsNull = false;
            while (mResultSet.next()) {
                mUserID = mResultSet.getInt("userid");
                stateUserID.set(0, mUserID);
                dataIsNull = true;
            }
            if (dataIsNull == false) {
                stateUserID.add(EX2016);
                return stateUserID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            stateUserID.add(EX2016);
            return stateUserID;
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
        return stateUserID;
    }

    private static final String UPDATE_USER_REGISTER = "UPDATE  aside_user set registerid=? where userid=?";

    @Override
    public int updateUserRegister(int mUserID, String registerID) {
        if (mUserID <= 0) {
            return ER5001;
        }

        try {
            mPreState = mConnection.prepareStatement(UPDATE_USER_REGISTER);
            mPreState.setString(1, registerID);
            mPreState.setInt(2, mUserID);
            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            closeMemberPreparedStatement();
        }

        return S1000;
    }

    private static final String SELECT_USER_REGISTER = "SELECT　registerid from aside_user where userid=?";

    @Override
    public int selectUserRegister(int mUserID, String registerID) {
        String registerid = " ";
        if (mUserID <= 0) {
            return ER5001;
        }

        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_REGISTER);
            mPreState.setInt(1, mUserID);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                registerid = mResultSet.getString("registerid");
            }
            if (registerid.equals(registerID)) {
                return S1000;
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
    }

    private static final String SELECT_USER_STATUS = "SELECT　status from aside_user where userid=?";

    @Override
    public int selectUserStatus(int mUserID) {
        int status = 1;
        if (mUserID <= 0) {
            return ER5001;
        }

        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_STATUS);
            mPreState.setInt(1, mUserID);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                status = mResultSet.getInt("status");
            }
            if (status == 0) {
                return S1000;
            } else {
                return status;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
    }

    private static final String UPDATE_USER_STATUS = "UPDATE  aside_user set status=? where userid=?";

    @Override
    public int updateUserStatus(int mUserID) {
        if (mUserID <= 0) {
            return ER5001;
        }

        try {
            mPreState = mConnection.prepareStatement(UPDATE_USER_STATUS);
            mPreState.setInt(1, 1);
            mPreState.setInt(2, mUserID);
            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            closeMemberPreparedStatement();
        }
        return S1000;
    }

    private static final String SELECT_USER_EMAIL = "SELECT　email from aside_user where userid=?";

    @Override
    public String selectUserEmail(int mUserID) {
        String email = "";
        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_EMAIL);
            mPreState.setInt(1, mUserID);
            mResultSet = mPreState.executeQuery();
            while (mResultSet.next()) {
                email = mResultSet.getString("email");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeMemberResultSet();
            closeMemberPreparedStatement();
        }
        return email;
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

    /**
     * 关闭指定对象
     * 
     * @param mAutoCloseable
     *            待关闭的对象，为不定长参数
     */
    private void closeAutoCloseable(AutoCloseable... mAutoCloseableArray) {
        if (mAutoCloseableArray == null || mAutoCloseableArray.length == 0) {
            return;
        }
        for (AutoCloseable mAutoCloseable : mAutoCloseableArray) {
            try {
                mAutoCloseable.close();
            } catch (Exception e1) {
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
