package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.springbook.practice.domain.User;

public class UserDAO {
	
//	private SimpleConnectionMaker simpleConnectionMaker;
	private ConnectionMaker connectionMaker; //�������̽��� ���� ������Ʈ�� �����ϹǷ� ��ü�� Ŭ���� ������ �� �ʿ䰡 ����.
	private Connection c;
	private User user;
	
	//datasource ���
	private DataSource dataSource;
	
	//�����ڸ� ���ؼ� ���Թ޴� ���
//	public UserDAO(ConnectionMaker connectionMaker) {
////		simpleConnectionMaker = new SimpleConnectionMaker();
////		1) connectionMaker = new DConnectionMaker(); => ��Ÿ�� ���� �������谡 �̹� �ڵ�ӿ� �� �����Ǿ� ����
//		
//		/*
//		 * // * )�������� �˻��� �̿��ϴ� ��� AnnotationConfigApplicationContext context = new
//		 * AnnotationConfigApplicationContext(DaoFactory.class); this.connectionMaker =
//		 * context.getBean("connectionMaker", ConnectionMaker.class);
//		 */
//		this.connectionMaker = connectionMaker;
//	}

	//�����ڸ� ���ؼ� ���Թ޴� ���
	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}
	
	//dataSource���
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
//		Connection c = connectionMaker.makeConnection(); //�������̽��� ���ǵ� �޼ҵ带 ����ϹǷ�, Ŭ������ �ٲ�ٰ� �ص� �޼ҵ� �̸��� ������� ����
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
		
//		Connection c = connectionMaker.makeConnection();
		this.c = connectionMaker.makeConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
//		User user = new User();
//		user.setId(rs.getString("id"));
//		user.setName(rs.getString("name"));
//		user.setPassword(rs.getString("password"));
		
		this.user = new User();
		this.user.setId(rs.getString("id"));
		this.user.setName(rs.getString("name"));
		this.user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return this.user;
	}
}
