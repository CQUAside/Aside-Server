package com.round.aside.server.module.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;

import com.round.aside.server.DB.DataSource;
import com.round.aside.server.entity.AdvertisementEntity;
import com.round.aside.server.entity.InformAdsEntity;
import com.round.aside.server.entity.InformUsersEntity;
import com.round.aside.server.entity.PersonalCollectionEntity;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 数据库管理模块超级接口的实现类
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public final class DatabaseManagerImpl implements IDatabaseManager {

    private static final String CHECK_ACCOUNT_EXIST_FORMAT = "select userid from aside_user where account = ?";

    private static final String INSERT_USER_FORMAT = "INSERT into aside_user(userid, account, password) values(?, ?, ?)";

    // 上传广告时要用的SQL语句
    public static final String uploadingAdsql = "INSERT into aside_advertisement(AdID, Thumbnail_ID, CarrouselID, Title, Content, StartTime, Deadline, Money, Status, ClickCount, CollectCount, UserID) values(?, ?, ?,?,?,?,?,?,?,?,?,?)";

    // 审核广告时查询广告信息用的SQL语句
    public static final String queryAdsql = "select ad.Status, ad.UserID, ad.CollectCount, ad.ClickCount from aside_advertisement ad where ad.AdID = ?";

    // 更新广告时用的SQL语句，包括更新状态、点击量、收藏量。
    public static final String updateAdsql = "update aside_advertisement set Status = ?, ClickCount = ?, CollectCount = ? where AdID = ?";

    // 删除广告时用的SQL语句
    public static final String deleteAdsql = "delete from aside_advertisement where AdID = ?";

    // 用户收藏广告时插入一个收藏记录
    public static final String insertCollectionsql = "insert into aside_collection(UserID, AdID) values(?, ?)";

    // 用户取消收藏时，删除一个收藏记录
    public static final String deleteCollectionsql = "delete from aside_collection where AdID = ? and UserID = ?";

    // 用户举报别的用户时，向举报用户表插入一个举报记录
    public static final String insertInformUsersql = "insert into aside_informUser(UserID, InformedUserID, InformReason) values(?, ?, ?)";

    // 用户举报广告时，向举报广告表插入一个举报记录
    public static final String insertInformAdsql = "insert into aside_informAds(UserID, AdID) values(?, ?)";

    private static final String INSERT_USER_FULL_FORMAT = "INSERT into aside_user(userid, account, password,nickname,email,phonenum) values(?, ?, ?,?,?)";
    private static final String SELECT_USER_FORMAT = "SELECT userid from aside_user where account=? and password=?";
    private static final String SELECT_USER_STATUS = "SELECT　status from aside_user where userid=?";
    private static final String SELECT_USER_REGISTER = "SELECT　registerid from aside_user where userid=?";
    private static final String SELECT_USER_EMAIL = "SELECT　email from aside_user where userid=?";// 以上三条语句可合并
    private static final String UPDATE_USER_REGISTER = "UPDATE  aside_user set registerid=? where userid=?";
    private static final String UPDATE_USER_STATUS = "UPDATE  aside_user set status=? where userid=?";

    @Override
    public int checkAccountExistence(String mAccount) {
        if (StringUtil.isEmpty(mAccount)) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        ResultSet mResultSet = null;

        boolean mExistence = false;
        try {
            mPreState = mConnection.prepareStatement(CHECK_ACCOUNT_EXIST_FORMAT);
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
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
            if (mResultSet != null) {
                try {
                    mResultSet.close();
                } catch (Exception e1) {
                }
            }
        }

        return mExistence ? R6002 : S1000;
    }

    private static final String INSERT_PHONEAUTH_FORMAT = "insert into aside_authcode(phone, authcode, pastdue_time) values(?, ?, ?)";

    @Override
    public int stashRegisterPhoneAuthcode(String mPhone, String mAuthcode) {
        if (StringUtil.isEmpty(mPhone) || StringUtil.isEmpty(mAuthcode) || mAuthcode.length() != 4) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(INSERT_PHONEAUTH_FORMAT);
            mPreState.setString(1, mPhone);
            mPreState.setString(2, mAuthcode);
            mPreState.setLong(3, System.currentTimeMillis() + 5 * 60 * 1000);
            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2013;
        } finally {
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }

        return S1000;
    }

    @Override
    public int insertUser(int mUserID, String mAccount, String mPassword) {
        if (mUserID <= 0 || StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(INSERT_USER_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mAccount);
            mPreState.setString(3, mPassword);
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
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }

        return S1000;
    }

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

        Connection connection = DataSource.getConnection();
        if (connection == null)
            return EX2012;
        PreparedStatement preparedstatement = null;
        try {
            preparedstatement = connection.prepareStatement(uploadingAdsql);
            preparedstatement.setInt(1, ad.getAdID());
            preparedstatement.setInt(2, ad.getThumbnail_ID());
            preparedstatement.setInt(3, ad.getCarrouselID());
            preparedstatement.setString(4, ad.getTitle());
            preparedstatement.setString(5, ad.getContent());
            preparedstatement.setString(6, ad.getStartTime());
            preparedstatement.setString(7, ad.getDeadline());
            preparedstatement.setDouble(8, ad.getMoney());
            preparedstatement.setInt(9, ad.getStatus());
            preparedstatement.setInt(10, ad.getClickCount());
            preparedstatement.setInt(11, ad.getCollectCount());
            preparedstatement.setInt(12, ad.getUserID());
            preparedstatement.executeUpdate();
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
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                } catch (Exception e1) {
                }
            }
        }
        return S1000;
    }

    @Override
    public int deleteAD(int adID) {
        if (adID <= 0 || adID > 99999999)
            return ER5001;

        Connection connetction = DataSource.getConnection();
        if (connetction == null)
            return EX2012;

        PreparedStatement preparedstatement = null;
        try {
            preparedstatement = connetction.prepareStatement(deleteAdsql);
            preparedstatement.setInt(1, adID);
            preparedstatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2015;
        } finally {
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                    connetction.close();
                } catch (Exception e1) {
                }
            }
        }
        return S1000;
    }

    @Override
    public AdAndStatusCode queryAD(int adID) {
        AdAndStatusCode ad = new AdAndStatusCode();
        if (adID <= 0 || adID > 99999999)
            ad.setStatusCode(ER5001);
        Connection connetction = DataSource.getConnection();
        if (connetction == null)
            ad.setStatusCode(EX2012);

        PreparedStatement preparedstatement = null;

        try {
            preparedstatement = connetction.prepareStatement(queryAdsql);
            preparedstatement.setInt(1, adID);
            ResultSet rs = preparedstatement.executeQuery();
            while (rs.next()) {
                ad.setStatus(rs.getInt("Status"));
                ad.setClickCount(rs.getInt("ClickCount"));
                ad.setCollectCount(rs.getInt("CollectCount"));
                ad.setUserID(rs.getInt("UserID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ad.setStatusCode(EX2016);
        } finally {
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                } catch (Exception e1) {
                }
            }
        }
        return ad;

    }

    @Override
    public int updateAD(int adID, AdAndStatusCode ad) {
        if (adID <= 0 || adID > 99999999)
            ad.setStatusCode(ER5001);
        Connection connetction = DataSource.getConnection();
        if (connetction == null)
            ad.setStatusCode(EX2012);

        PreparedStatement preparedstatement = null;
        try {
            preparedstatement = connetction.prepareStatement(updateAdsql);
            preparedstatement.setInt(1, ad.getStatus());
            preparedstatement.setInt(2, ad.getClickCount());
            preparedstatement.setInt(3, ad.getCollectCount());
            preparedstatement.setInt(4, adID);

            preparedstatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                    connetction.close();
                } catch (Exception e1) {
                }
            }
        }
        return S1000;

    }

    public static class AdAndStatusCode {
        int Status;
        int ClickCount;
        int CollectCount;
        int UserID;
        int StatusCode;

        public int getStatusCode() {
            return StatusCode;
        }

        public void setStatusCode(int statusCode) {
            StatusCode = statusCode;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public int getClickCount() {
            return ClickCount;
        }

        public void setClickCount(int clickCount) {
            ClickCount = clickCount;
        }

        public int getCollectCount() {
            return CollectCount;
        }

        public void setCollectCount(int collectCount) {
            CollectCount = collectCount;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }

    }

    @Override
    public int insertCollection(PersonalCollectionEntity personalCollection) {
        if (personalCollection.getAdID() <= 0
                || personalCollection.getAdID() > 99999999)
            return ER5001;
        if (personalCollection.getUserID() <= 0
                || personalCollection.getUserID() > 99999999) {
            return ER5001;
        }
        Connection connection = DataSource.getConnection();
        if (connection == null)
            return EX2012;
        PreparedStatement preparedstatement = null;

        try {
            preparedstatement = connection
                    .prepareStatement(insertCollectionsql);
            preparedstatement.setInt(1, personalCollection.getUserID());
            preparedstatement.setInt(2, personalCollection.getAdID());
            preparedstatement.executeUpdate();
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
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                    connection.close();
                } catch (Exception e1) {
                }
            }
        }
        return S1000;
    }

    @Override
    public int deleteCollecion(int adID, int userID) {
        if (adID <= 0 || adID > 99999999)
            return ER5001;
        if (userID <= 0 || userID > 99999999) {
            return ER5001;
        }
        Connection connection = DataSource.getConnection();
        if (connection == null)
            return EX2012;
        PreparedStatement preparedstatement = null;
        try {
            preparedstatement = connection
                    .prepareStatement(deleteCollectionsql);
            preparedstatement.setInt(1, adID);
            preparedstatement.setInt(2, userID);
            preparedstatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2015;
        } finally {
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                    connection.close();
                } catch (Exception e1) {
                }
            }
        }
        return S1000;
    }

    @Override
    public int insertInformAd(InformAdsEntity informAd) {
        if (informAd.getAdID() <= 0 || informAd.getAdID() > 99999999)
            return ER5001;
        if (informAd.getUserID() <= 0)
            return ER5001;

        Connection connection = DataSource.getConnection();
        if (connection == null)
            return EX2012;
        PreparedStatement preparedstatement = null;
        try {
            preparedstatement = connection.prepareStatement(insertInformAdsql);
            preparedstatement.setInt(1, informAd.getUserID());
            preparedstatement.setInt(2, informAd.getAdID());

            preparedstatement.executeUpdate();
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
            if (preparedstatement != null) {
                try {
                    preparedstatement.close();
                } catch (Exception e1) {
                }
            }
        }
        return S1000;
    }

    @Override
    public int insertInformUser(InformUsersEntity informUser) {
        if (informUser.getUserID() <= 0)
            return ER5001;
        if (informUser.getInformedUserID() <= 0)
            return ER5001;
        Connection connection = DataSource.getConnection();
        if (connection == null)
            return EX2012;
        PreparedStatement preparedstatement = null;
        try {
            preparedstatement = connection
                    .prepareStatement(insertInformUsersql);
            preparedstatement.setInt(1, informUser.getUserID());
            preparedstatement.setInt(2, informUser.getInformedUserID());
            preparedstatement.setString(3, informUser.getInformReason());

            preparedstatement.executeUpdate();
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
        }

        return S1000;
    }

    @Override
    public int insertUser(int mUserID, String mAccount, String mPassword,
            String NickName, String Email, String PhoneNum) {
        if (mUserID <= 0 || mUserID > 99999999 || StringUtil.isEmpty(mAccount)
                || StringUtil.isEmpty(mPassword)) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(INSERT_USER_FULL_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mAccount);
            mPreState.setString(3, mPassword);
            mPreState.setString(4, NickName);
            mPreState.setString(5, Email);
            mPreState.setString(6, PhoneNum);
            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            String mExMsg = e.getMessage();
            if (mExMsg.indexOf("userid") != -1) {
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
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }

        return S1000;
    }

    @Override
    public LinkedList<Integer> selectUser(String mAccount, String mPassword) {
        LinkedList<Integer> stateUserID = new LinkedList<Integer>();
        int mUserID = 0;
        stateUserID.add(mUserID);
        if (StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)) {
            stateUserID.add(ER5001);
            return stateUserID;
        }
        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            stateUserID.add(EX2012);
            return stateUserID;
        }
        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_FORMAT);
            mPreState.setString(1, mAccount);
            mPreState.setString(2, mPassword);
            ResultSet rs = mPreState.executeQuery();
            boolean dataIsNull = false;
            while (rs.next()) {
                mUserID = rs.getInt("userid");
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
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }
        return stateUserID;
    }

    @Override
    public int updateUserRegister(int mUserID, String registerID) {
        if (mUserID <= 0) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(UPDATE_USER_REGISTER);
            mPreState.setString(1, registerID);
            mPreState.setInt(2, mUserID);
            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }

        return S1000;
    }

    @Override
    public int selectUserRegister(int mUserID, String registerID) {
        String registerid = " ";
        if (mUserID <= 0) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_REGISTER);
            mPreState.setInt(1, mUserID);
            ResultSet rs = mPreState.executeQuery();
            while (rs.next()) {
                registerid = rs.getString("registerid");
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
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }
    }

    public int selectUserStatus(int mUserID) {
        int status = 1;
        if (mUserID <= 0) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }
        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_STATUS);
            mPreState.setInt(1, mUserID);
            ResultSet rs = mPreState.executeQuery();
            while (rs.next()) {
                status = rs.getInt("status");
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
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }
    }

    public int updateUserStatus(int mUserID) {
        if (mUserID <= 0) {
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(UPDATE_USER_STATUS);
            mPreState.setInt(1, 1);
            mPreState.setInt(2, mUserID);
            mPreState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return EX2014;
        } finally {
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }
        return S1000;
    }

    public String selectUserEmail(int mUserID) {
        String email = "";
        Connection mConnection = DataSource.getConnection();
        PreparedStatement mPreState = null;
        try {
            mPreState = mConnection.prepareStatement(SELECT_USER_EMAIL);
            mPreState.setInt(1, mUserID);
            ResultSet rs = mPreState.executeQuery();
            while (rs.next()) {
                email = rs.getString("email");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (mPreState != null) {
                try {
                    mPreState.close();
                } catch (Exception e1) {
                }
            }
        }
        return email;
    }

}