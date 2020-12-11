package com.springbook.practice.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

//	@Bean
//	public UserDAO userDAO() {
//		//모든 DAO는 결국 DconnctionMaker를 DI받는데, 그 중간다리에 CountingConnectionMaker가 꼈음
////		return new UserDAO(connectionMaker());
//		
//		//생성자를 통해 주입하도록 바꾼 경우
//		UserDAO userDAO = new UserDAO();
//		userDAO.setConnectionMaker(connectionMaker());
//		
//		return userDAO;
//	}
	
	//xml 로 변환하는 경우
	@Bean //<bean
	public ConnectionMaker connectionMaker() { //id="connectionMaker"
		return new CountingConnectionMaker(realConnectionMaker()); //class="com.springbook.practice.dao.CountingConnectionMaker" />
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new DConnectionMaker();
	}
}
