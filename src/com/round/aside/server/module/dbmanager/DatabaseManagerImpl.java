package com.round.aside.server.module.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.round.aside.server.DB.DataSource;
import com.round.aside.server.constant.StatusCode;

/**
 * 数据库管理模块超级接口的实现类
 * 
 * @author A Shuai
 * @date 2016-3-28
 *
 */
public final class DatabaseManagerImpl implements IDatabaseManager{

    private static final String INSERT_USERID_FORMAT = "INSERT into aside_user(id) values(?)";
    
    @Override
    public int insertUserID(int mUserID) {
        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return StatusCode.EX2011;
        }
        
        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(INSERT_USERID_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            return StatusCode.EX2012;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
        
        return StatusCode.S1000;
    }
    
    private static final String UPDATE_USER1_FORMAT = "UPDATE aside_user SET username = ?, password = ? WHERE id = ?";
    
    @Override
    public int updateUser(int mUserID, String mAccount, String mPassword) {
        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return StatusCode.EX2011;
        }
        
        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(UPDATE_USER1_FORMAT);
            mPreState.setString(1, mAccount);
            mPreState.setString(2, mPassword);
            mPreState.setInt(3, mUserID);
            mPreState.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            return StatusCode.EX2012;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
        
        return StatusCode.S1000;
    }
    
}
