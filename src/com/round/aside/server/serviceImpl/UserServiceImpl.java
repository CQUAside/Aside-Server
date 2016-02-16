package com.round.aside.server.serviceImpl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.round.aside.server.dao.UserDao;
import com.round.aside.server.pojo.User;
import com.round.aside.server.service.UserService;

@Component
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 按用户名查询
	 * @param username
	 * @return
	 */
	public User getUser(String username) {
		return userDao.getUserbyname(username);
	}
	
	public User login() {
		//TODO
		return null;
	}
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public boolean register(User user) {
		int id = generateID();
		System.out.println("生成ID:"+id);
		return userDao.addUser(user,id);
	}

	/**
	 * 生成用户ID
	 */
	public int generateID() {
		int id = (int) (1+Math.random()*1000000000);
		while(userDao.getUserByID(id)){
			id = (int) (1+Math.random()*1000000000);
		}
		return id;
	}
}
