package com.round.aside.server.service;

import org.springframework.stereotype.Component;

import com.round.aside.server.pojo.User;

@Component
public interface UserService {
	
	public User getUser(String username);
	
	public User login();
	
	public boolean register(User user);
	
	public int generateID();
}
