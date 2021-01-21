package com.springbook.practice.dao;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;

public class UserService {

	UserDAO userDAO;
	
	UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
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
	
	public void upgradeLevels() throws Exception {
		TransactionSynchronizationManager.initSynchronization();
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try {
			List<User> users = userDAO.getAll();
			for(User user : users) {
				if(canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		} finally {
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
		
	}
	
	protected boolean canUpgradeLevel(User user) {
		return userLevelUpgradePolicy.canUpgradeLevel(user);
	}
	protected void upgradeLevel(User user) {
		userLevelUpgradePolicy.upgradeLevel(user);
	}

}
