package com.round.aside.server.module.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.round.aside.server.DB.DataSource;
import com.round.aside.server.util.StringUtil;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 数据库管理模块超级接口的实现类
 * 
 * @author A Shuai
 * @date 2016-3-28
 *
 */
public final class DatabaseManagerImpl implements IDatabaseManager{

    private static final String INSERT_USER_FORMAT = "INSERT into aside_user(userid, account, password) values(?, ?, ?)";
    
    @Override
    public int insertUser(int mUserID, String mAccount, String mPassword) {
        if(mUserID <= 0 || mUserID > 99999999 || StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)){
            return ER5001;
        }
        
        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }
        
        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(INSERT_USER_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mAccount);
            mPreState.setString(3, mPassword);
            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e){
            String mExMsg = e.getMessage();
            if(mExMsg.indexOf("userid'") != -1){
                return F8001;
            } else if (mExMsg.indexOf("account") != -1){
                return F8002;
            } else {
                e.printStackTrace();
                throw new IllegalStateException("This is a improper exception, please check your code!");
            }
        } catch (SQLException e){
            e.printStackTrace();
            return EX2013;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
        
        return S1000;
    }
    
}
