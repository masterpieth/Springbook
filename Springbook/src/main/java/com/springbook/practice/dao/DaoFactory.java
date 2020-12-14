package com.springbook.practice.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration //애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {
	
	
//	@Bean //오브젝트 생성을 담당하는 IoC용 메소드라는 표시
//	public UserDAO userDAO() {
		//userdao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정한다.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDAO dao = new UserDAO(connectionMaker);
//		return dao;
		
//		connectionMaker 오브젝트를 반환하는 메소드를 따로 빼서 중복코드를 제거함(리팩토링)
//		return new UserDAO(connectionMaker());
		
		//생성자를 통해 주입받는 경우
//		UserDAO userDAO = new UserDAO();
//		userDAO.setConnectionMaker(connectionMaker());
//		userDAO.setDataSource(dataSource());
//		return userDAO;
//	}
	
//	2020-11-19 daofactory는 DI 컨테이너: DconnectionMaker 오브젝트의 레퍼런스를 userdao에 전달하여,
//	런타임시에 둘의 의존관계를 만드는 역할을 함
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
	
	//2020-11-23 dataSource 를 사용한 DI
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		//DB연결정보 -> 오브젝트 레벨에서 DB 연결방식을 변경할 수 있음
																	//xml에서 설정하는 경우
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class); 	//<property name="driverClass" value="com.mysql.jdbc.Driver"/>
		dataSource.setUrl("jdbc:mysql://localhost/springbook");		//<property name="url" value="jdbc:mysql://localhost/springbook"/>
		dataSource.setUsername("root");								//<property name="username" value="root"/>
		dataSource.setPassword("1234");								//<property name="password" value="1234"/>
		
		return dataSource;
	}
}