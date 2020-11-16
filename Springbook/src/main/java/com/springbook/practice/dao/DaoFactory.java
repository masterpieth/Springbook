package com.springbook.practice.dao;

public class DaoFactory {
	public UserDAO userDAO() {
		//userdao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정한다.
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDAO dao = new UserDAO(connectionMaker);
		return dao;
	}
}
