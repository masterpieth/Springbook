package com.springbook.practice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.dao.UserService;
import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;

@ContextConfiguration(locations = "/test-applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

	@Autowired
	UserService userService;
	
	@Autowired
	UserDAO userDAO;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("wronggim1", "nyk1", "1234", Level.BASIC, 49, 0),
				new User("wronggim2", "nyk2", "12345", Level.BASIC, 50, 0),
				new User("wronggim3", "nyk3", "123456", Level.SILVER, 60, 29),
				new User("wronggim4", "nyk4", "1234567", Level.SILVER, 60, 30),
				new User("wronggim5", "nyk5", "12345678", Level.GOLD, 100, 100)
		);
	}
	
	//빈 주입 확인용
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() {
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
}

