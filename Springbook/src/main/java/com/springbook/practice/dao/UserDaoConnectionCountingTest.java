package com.springbook.practice.dao;

import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.springbook.practice.domain.User;

public class UserDaoConnectionCountingTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
		//DAO 사용 이후~
		User user = new User();
		user.setId("wronggim");
		user.setName("김나영");
		user.setPassword("1234");
		
		dao.add(user);
		//결과 확인
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());
	}
}
