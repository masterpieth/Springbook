package com.springbook.practice.dao;

public class DaoFactory {
	public UserDAO userDAO() {
		//userdao Ÿ���� ������Ʈ�� ��� �����, ��� �غ��ų���� �����Ѵ�.
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDAO dao = new UserDAO(connectionMaker);
		return dao;
	}
}
