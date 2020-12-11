package com.springbook.practice.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration //���ø����̼� ���ؽ�Ʈ �Ǵ� �� ���丮�� ����� ����������� ǥ��
public class DaoFactory {
	
	
//	@Bean //������Ʈ ������ ����ϴ� IoC�� �޼ҵ��� ǥ��
//	public UserDAO userDAO() {
		//userdao Ÿ���� ������Ʈ�� ��� �����, ��� �غ��ų���� �����Ѵ�.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDAO dao = new UserDAO(connectionMaker);
//		return dao;
		
//		connectionMaker ������Ʈ�� ��ȯ�ϴ� �޼ҵ带 ���� ���� �ߺ��ڵ带 ������(�����丵)
//		return new UserDAO(connectionMaker());
		
		//�����ڸ� ���� ���Թ޴� ���
//		UserDAO userDAO = new UserDAO();
//		userDAO.setConnectionMaker(connectionMaker());
//		userDAO.setDataSource(dataSource());
//		return userDAO;
//	}
	
//	2020-11-19 daofactory�� DI �����̳�: DconnectionMaker ������Ʈ�� ���۷����� userdao�� �����Ͽ�,
//	��Ÿ�ӽÿ� ���� �������踦 ����� ������ ��
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
	
	//2020-11-23 dataSource �� ����� DI
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		//DB�������� -> ������Ʈ �������� DB �������� ������ �� ����
																	//xml���� �����ϴ� ���
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class); 	//<property name="driverClass" value="com.mysql.jdbc.Driver"/>
		dataSource.setUrl("jdbc:mysql://localhost/springbook");		//<property name="url" value="jdbc:mysql://localhost/springbook"/>
		dataSource.setUsername("root");								//<property name="username" value="root"/>
		dataSource.setPassword("1234");								//<property name="password" value="1234"/>
		
		return dataSource;
	}
}
