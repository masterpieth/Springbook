package com.springbook.practice.dao;

public class DaoFactory {
	public UserDAO userDAO() {
		//userdao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정한다.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//		UserDAO dao = new UserDAO(connectionMaker);
//		return dao;
		
//		connectionMaker 오브젝트를 반환하는 메소드를 따로 빼서 중복코드를 제거함(리팩토링)
		return new UserDAO(connectionMaker());
	}
	
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
