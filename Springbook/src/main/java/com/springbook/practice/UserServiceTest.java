package com.springbook.practice;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.dao.UserLevelUpgradePolicy;
import com.springbook.practice.dao.UserLevelUpgradePolicyCommon;
import com.springbook.practice.dao.UserService;
import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;

import static com.springbook.practice.dao.UserLevelUpgradePolicyCommon.MIN_LOGOUT_FOR_SILVER;
import static com.springbook.practice.dao.UserLevelUpgradePolicyCommon.MIN_RECOMMEND_FOR_GOLD;

@ContextConfiguration(locations = "/test-applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

	@Autowired
	UserService userService;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	List<User> users;
	
	static class TestUserServiceException extends RuntimeException{
		
	}
	
	static class TestUserService extends UserService {
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			
			super.upgradeLevel(user);
		}
	}
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("wronggim1", "nyk1", "1234", Level.BASIC, MIN_LOGOUT_FOR_SILVER-1, 0),
				new User("wronggim2", "nyk2", "12345", Level.BASIC, MIN_LOGOUT_FOR_SILVER, 0),
				new User("wronggim3", "nyk3", "123456", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
				new User("wronggim4", "nyk4", "1234567", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
				new User("wronggim5", "nyk5", "12345678", Level.GOLD, 100, Integer.MAX_VALUE)
		);
	}
	
	//빈 주입 확인용
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		userDAO.deleteAll();
		for(User user : users) userDAO.add(user);
		
		userService.upgradeLevels();
	
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}
	
	//신버전 checkLevel
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDAO.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}
		else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	//구버전 checkLevel -> assertThat의 두번째 파라미터가 level값
	@Autowired(required = false)
	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDAO.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
	
	@Test
	public void add() {
		userDAO.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDAO.get(userWithLevel.getId());
		User userWithoutLevelRead = userDAO.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		
		UserService testUserService = new TestUserService(users.get(3).getId());
		UserLevelUpgradePolicyCommon policy = new UserLevelUpgradePolicyCommon();
		policy.setUserDAO(this.userDAO);
		
		testUserService.setUserDAO(this.userDAO);
		testUserService.setUserLevelUpgradePolicy(policy);
		testUserService.setTransactionManager(transactionManager);
		
		userDAO.deleteAll();
		for(User user : users) userDAO.add(user);
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		checkLevelUpgraded(users.get(1), false);
	}
}

