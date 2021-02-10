package com.springbook.practice;

import static com.springbook.practice.service.UserLevelUpgradePolicyCommon.MIN_LOGOUT_FOR_SILVER;
import static com.springbook.practice.service.UserLevelUpgradePolicyCommon.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;
import com.springbook.practice.service.UserLevelUpgradePolicy;
import com.springbook.practice.service.UserLevelUpgradePolicyCommon;
import com.springbook.practice.service.UserServiceImpl;
import com.springbook.practice.service.UserServiceTx;
import com.springbook.practice.test.MockMailSender;

@ContextConfiguration(locations = "/test-applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	MailSender mailSender;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("wronggim1", "nyk1", "1234", Level.BASIC, MIN_LOGOUT_FOR_SILVER-1, 0, "ffpieth@gmail.com"),
				new User("wronggim2", "nyk2", "12345", Level.BASIC, MIN_LOGOUT_FOR_SILVER, 0, "ffpieth@gmail.com"),
				new User("wronggim3", "nyk3", "123456", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "ffpieth@gmail.com"),
				new User("wronggim4", "nyk4", "1234567", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "ffpieth@gmail.com"),
				new User("wronggim5", "nyk5", "12345678", Level.GOLD, 100, Integer.MAX_VALUE, "ffpieth@gmail.com")
		);
	}
	
	//빈 주입 확인용
	@Test
	public void bean() {
		assertThat(this.userServiceImpl, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDAO mockUserDAO = new MockUserDAO(this.users);
		userServiceImpl.setUserDAO(mockUserDAO);
		
		MockMailSender mockMailSender = new MockMailSender();
		
		UserLevelUpgradePolicyCommon policy = new UserLevelUpgradePolicyCommon();
		policy.setUserDAO(mockUserDAO);
		policy.setMailSender(mockMailSender);
		
		userServiceImpl.setUserLevelUpgradePolicy(policy);
		
		userServiceImpl.upgradeLevels();
	
		List<User> updated = mockUserDAO.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "wronggim2", Level.SILVER);
		checkUserAndLevel(updated.get(1), "wronggim4", Level.GOLD);
		
		List<String> reauest = mockMailSender.getRequests();
		assertThat(reauest.size(), is(2));
		assertThat(reauest.get(0), is(users.get(1).getEmail()));
		assertThat(reauest.get(1), is(users.get(3).getEmail()));
	}
	
	//신신버전
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
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
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		mock(UserDAO.class);
//		UserDAO mockUserDAO = mock(UserDAO.class);
	}
	@Test
	public void add() {
		userDAO.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userServiceImpl.add(userWithLevel);
		userServiceImpl.add(userWithoutLevel);
		
		User userWithLevelRead = userDAO.get(userWithLevel.getId());
		User userWithoutLevelRead = userDAO.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		UserLevelUpgradePolicyCommon policy = new UserLevelUpgradePolicyCommon();
		policy.setUserDAO(this.userDAO);
		policy.setMailSender(mailSender);
		
		testUserService.setUserDAO(this.userDAO);
		testUserService.setUserLevelUpgradePolicy(policy);
		
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);
		
		userDAO.deleteAll();
		for(User user : users) userDAO.add(user);
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		checkLevelUpgraded(users.get(1), false);
	}
	
	static class TestUserServiceException extends RuntimeException{
		
	}
	
	static class TestUserService extends UserServiceImpl {
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			
			super.upgradeLevel(user);
		}
	}
	
	static class MockUserDAO implements UserDAO {

		private List<User> users;
		private List<User> updated = new ArrayList<User>();
		
		private MockUserDAO(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated() {
			return this.updated;
		}
		
		@Override
		public void update(User user) {
			updated.add(user);
		}

		@Override
		public List<User> getAll() {
			return this.users;
		}

		@Override
		public void deleteAll() { throw new UnsupportedOperationException(); }
		@Override
		public int getCount() { throw new UnsupportedOperationException(); }
		@Override
		public void add(User user) { throw new UnsupportedOperationException(); }
		
		@Override
		public User get(String id) { throw new UnsupportedOperationException(); }


	}
}

