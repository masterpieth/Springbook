package com.springbook.practice;

import java.sql.SQLException;

import com.springbook.practice.dao.ConnectionMaker;
import com.springbook.practice.dao.DConnectionMaker;
import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.domain.User;

public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//userdao가 사용할 connectionmaker 구현 클래스를 결정하고 오브젝트를 만든다.
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		//dao에게 넘겨줌으로써 두 오브젝트 사이의 의존관계가 설정된다.
		UserDAO dao = new UserDAO(connectionMaker);
		//위 작업은 userDao와 connectionMaker 구현 클래스와의 런타임 오브젝트 의존관계를 설정하는 책임을 클라이언트로 가져온 결과이다.
		
		User user = new User();
		user.setId("wronggim");
		user.setName("김나영");
		user.setPassword("1234");
		
		dao.add(user);
		
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + "조회 성공");
	}
}
