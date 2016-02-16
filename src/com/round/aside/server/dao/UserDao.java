package com.round.aside.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.round.aside.server.DB.DataSource;
import com.round.aside.server.pojo.User;

@Component
public class UserDao {
	
	/**
	 * 按用户名查询
	 * @param username
	 * @return
	 */
	public User getUserbyname(String username) {
		String sql="select * from aside_user where username ='"+username+"'";
		Connection con = DataSource.getConnection();
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				User u= new User();
				u.setUsername(rs.getString("username")) ;
				u.setId(rs.getString("id"));
				u.setNickname(rs.getString("nickname"));
				return u;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 注册用户
	 * @param user
	 * @param id 
	 * @return
	 */
	public boolean addUser(User user, int id) {
		String sql="insert into aside_user(id,username) values("+id+",'"+user.getUsername()+"')";
		Connection con = DataSource.getConnection();
		try {
			con.createStatement().executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println("用户"+user.getUsername()+"添加出错");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 按ID查找用户
	 * @param id
	 * @return
	 */
	public boolean getUserByID(int id) {
		String sql="select id from aside_user where id = "+id+"";
		Connection con = DataSource.getConnection();
			ResultSet rs;
			try {
				rs = con.createStatement().executeQuery(sql);
				if(rs.next())
					return true;
				else
					return false;
			} catch (SQLException e) {
				System.out.println("ID"+id+"查询添加出错");
				e.printStackTrace();
				return true;
			}
	}
	
}
