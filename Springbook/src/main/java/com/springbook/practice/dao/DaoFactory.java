package com.springbook.practice.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //���ø����̼� ���ؽ�Ʈ �Ǵ� �� ���丮�� ����� ����������� ǥ��
public class DaoFactory {
	
	@Bean //������Ʈ ������ ����ϴ� IoC�� �޼ҵ��� ǥ��
	public UserDAO userDAO() {
		//userdao Ÿ���� ������Ʈ�� ��� �����, ��� �غ��ų���� �����Ѵ�.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDAO dao = new UserDAO(connectionMaker);
//		return dao;
		
//		connectionMaker ������Ʈ�� ��ȯ�ϴ� �޼ҵ带 ���� ���� �ߺ��ڵ带 ������(�����丵)
		return new UserDAO(connectionMaker());
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
