package com.springbook.practice.dao;

import java.util.List;

import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;

public class UserService {

	UserDAO userDAO;
	
	public void setUserDAO(UserDAO userDao) {
		this.userDAO = userDao;
	}
	
	public void upgradeLevels() {
		List<User> users = userDAO.getAll();
		for(User user : users) {
			Boolean changed = null;
			if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
				user.setLevel(Level.SILVER);
				changed = true;
			}
			else if(user.getLevel() == Level.SILVER && user.getRecommend() >=30) {
				user.setLevel(Level.GOLD);
				changed = true;
			}
			else if(user.getLevel() == Level.GOLD) {changed = false;}
			else {changed = false;}
			if(changed) {userDAO.update(user);}
		}
	}
}
