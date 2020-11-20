package com.springbook.practice.dao;

import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.springbook.practice.domain.User;

public class UserDaoConnectionCountingTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
		//DAO ��� ����~
		User user = new User();
		user.setId("wronggim");
		user.setName("�質��");
		user.setPassword("1234");
		
		dao.add(user);
		//��� Ȯ��
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());
	}
}
