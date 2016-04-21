package com.round.aside.server.module.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;

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
    private static final String INSERT_USER_FULL_FORMAT = "INSERT into aside_user(userid, account, password,nickname,email,phonenum) values(?, ?, ?,?,?)";
    private static final String SELECT_USER_FORMAT="SELECT userid from aside_user where account=? and password=?";
    private static final String SELECT_USER_STATUS="SELECT　status from aside_user where userid=?";
    private static final String SELECT_USER_REGISTER="SELECT　registerid from aside_user where userid=?";
    private static final String SELECT_USER_EMAIL="SELECT　email from aside_user where userid=?";//以上三条语句可合并
    private static final String UPDATE_USER_REGISTER="UPDATE  aside_user set registerid=? where userid=?";
    private static final String UPDATE_USER_STATUS="UPDATE  aside_user set status=? where userid=?";
    
    
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
            if(mExMsg.indexOf("userid") != -1){
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
    
    @Override
    public int insertUser(int mUserID, String mAccount, String mPassword,String NickName,String Email,String PhoneNum) {
        if(mUserID <= 0 || mUserID > 99999999 || StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)){
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(INSERT_USER_FULL_FORMAT);
            mPreState.setInt(1, mUserID);
            mPreState.setString(2, mAccount);
            mPreState.setString(3, mPassword);
            mPreState.setString(4, NickName);
            mPreState.setString(5, Email);
            mPreState.setString(6, PhoneNum);
            mPreState.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e){
            String mExMsg = e.getMessage();
            if(mExMsg.indexOf("userid") != -1){
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

    @Override
    public LinkedList<Integer> selectUser(String mAccount,String mPassword){
        LinkedList<Integer> stateUserID=new LinkedList<Integer>();
        int mUserID=0;
        stateUserID.add(mUserID);
        if(StringUtil.isEmpty(mAccount) || StringUtil.isEmpty(mPassword)){
            stateUserID.add( ER5001);
            return stateUserID;
        }
        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            stateUserID.add( EX2012);
            return stateUserID;
        }
        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(SELECT_USER_FORMAT);
            mPreState.setString(1, mAccount);
            mPreState.setString(2, mPassword);
            ResultSet rs= mPreState.executeQuery();
            boolean dataIsNull=false;
            while(rs.next()){
                mUserID=rs.getInt("userid");
                stateUserID.set(0, mUserID);
                dataIsNull=true;
            }
            if( dataIsNull==false){
                stateUserID.add( EX2016);
                return stateUserID;
            }
        }  catch (SQLException e){
            e.printStackTrace();
            stateUserID.add( EX2016);
            return stateUserID;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
        return stateUserID;
    }

    @Override
    public int updateUserRegister(int mUserID,String registerID){
        if(mUserID <= 0 || mUserID > 99999999 ){
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(UPDATE_USER_REGISTER);
            mPreState.setString(1, registerID);
            mPreState.setInt(2, mUserID);           
            mPreState.executeUpdate();
        }  catch (SQLException e){
            e.printStackTrace();
            return EX2014;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }

        return S1000;
    }

    @Override
    public int selectUserRegister(int mUserID,String registerID){
        String registerid=" ";
        if(mUserID <= 0 || mUserID > 99999999 ){
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(SELECT_USER_REGISTER);
            mPreState.setInt(1, mUserID);           
            ResultSet rs= mPreState.executeQuery();
            while(rs.next()){
                registerid=rs.getString("registerid");       	
            }
            if(	registerid.equals(registerID)){
                return S1000;
            }
            else{
                return 0;
            }

        }  catch (SQLException e){
            e.printStackTrace();
            return EX2014;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
    }



    public int selectUserStatus(int mUserID){
        int status=1;
        if(mUserID <= 0 || mUserID > 99999999 ){
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }
        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(SELECT_USER_STATUS);
            mPreState.setInt(1, mUserID);           
            ResultSet rs= mPreState.executeQuery();
            while(rs.next()){
                status=rs.getInt("status");       	
            }
            if(status==0){
                return S1000;
            }
            else{
                return status;
            }

        }  catch (SQLException e){
            e.printStackTrace();
            return EX2014;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
    }


    public int updateUserStatus(int mUserID){
        if(mUserID <= 0 || mUserID > 99999999 ){
            return ER5001;
        }

        Connection mConnection = DataSource.getConnection();
        if (mConnection == null) {
            return EX2012;
        }

        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement( UPDATE_USER_STATUS);
            mPreState.setInt(1, 1);
            mPreState.setInt(2, mUserID);           
            mPreState.executeUpdate();
        }  catch (SQLException e){
            e.printStackTrace();
            return EX2014;
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
        return S1000;
    }
    public String selectUserEmail(int mUserID){ 
        String email="";
        Connection mConnection = DataSource.getConnection();     
        PreparedStatement mPreState = null;
        try{
            mPreState = mConnection.prepareStatement(SELECT_USER_EMAIL);
            mPreState.setInt(1, mUserID);           
            ResultSet rs= mPreState.executeQuery();
            while(rs.next()){
                email=rs.getString("email");       	
            }             

        }  catch (SQLException e){
            e.printStackTrace();      
        } finally {
            if(mPreState != null){
                try{
                    mPreState.close();
                } catch (Exception e1){  }
            }
        }
        return email;
    }
}
