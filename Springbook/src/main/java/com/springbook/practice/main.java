package com.springbook.practice;

import java.sql.SQLException;

import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.domain.User;

public class main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		UserDAO dao = new UserDAO();
		
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
