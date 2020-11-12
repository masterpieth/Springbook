package com.springbook.practice;

import java.sql.SQLException;

import com.springbook.practice.dao.ConnectionMaker;
import com.springbook.practice.dao.DConnectionMaker;
import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.domain.User;

public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//userdao�� ����� connectionmaker ���� Ŭ������ �����ϰ� ������Ʈ�� �����.
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		//dao���� �Ѱ������ν� �� ������Ʈ ������ �������谡 �����ȴ�.
		UserDAO dao = new UserDAO(connectionMaker);
		//�� �۾��� userDao�� connectionMaker ���� Ŭ�������� ��Ÿ�� ������Ʈ �������踦 �����ϴ� å���� Ŭ���̾�Ʈ�� ������ ����̴�.
		
		User user = new User();
		user.setId("wronggim");
		user.setName("�質��");
		user.setPassword("1234");
		
		dao.add(user);
		
		System.out.println(user.getId() + "��� ����");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + "��ȸ ����");
	}
}
