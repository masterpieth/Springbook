package com.springbook.practice.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {
	
	@Bean //오브젝트 생성을 담당하는 IoC용 메소드라는 표시
	public UserDAO userDAO() {
		//userdao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정한다.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDAO dao = new UserDAO(connectionMaker);
//		return dao;
		
//		connectionMaker 오브젝트를 반환하는 메소드를 따로 빼서 중복코드를 제거함(리팩토링)
		return new UserDAO(connectionMaker());
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
