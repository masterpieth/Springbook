package com.springbook.practice.dao;

import java.util.List;

import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;

public class UserService {

	UserDAO userDAO;
	UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	public void setUserDAO(UserDAO userDao) {
		this.userDAO = userDao;
	}
	
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}

	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDAO.add(user);
	}
	
	public void upgradeLevels() {
		List<User> users = userDAO.getAll();
		for(User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}
	
	private boolean canUpgradeLevel(User user) {
		return userLevelUpgradePolicy.canUpgradeLevel(user);
	}
	protected void upgradeLevel(User user) {
		userLevelUpgradePolicy.upgradeLevel(user);
	}

}
