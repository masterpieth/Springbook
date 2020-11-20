package com.springbook.practice.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

	@Bean
	public UserDAO userDAO() {
		//��� DAO�� �ᱹ DconnctionMaker�� DI�޴µ�, �� �߰��ٸ��� CountingConnectionMaker�� ����
//		return new UserDAO(connectionMaker());
		
		//�����ڸ� ���� �����ϵ��� �ٲ� ���
		UserDAO userDAO = new UserDAO();
		userDAO.setConnectionMaker(connectionMaker());
		
		return userDAO;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new DConnectionMaker();
	}
}
