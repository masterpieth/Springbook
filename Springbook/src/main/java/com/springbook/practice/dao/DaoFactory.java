package com.springbook.practice.dao;

public class DaoFactory {
	public UserDAO userDAO() {
		//userdao Ÿ���� ������Ʈ�� ��� �����, ��� �غ��ų���� �����Ѵ�.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDAO dao = new UserDAO(connectionMaker);
//		return dao;
		
//		connectionMaker ������Ʈ�� ��ȯ�ϴ� �޼ҵ带 ���� ���� �ߺ��ڵ带 ������(�����丵)
		return new UserDAO(connectionMaker());
	}
	
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
