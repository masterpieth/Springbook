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
	private ConnectionMaker connectionMaker; //인터페이스를 통해 오브젝트에 접근하므로 구체적 클래스 정보를 알 필요가 없다.
	private Connection c;
	private User user;
	
	//datasource 사용
	private DataSource dataSource;
	
	//생성자를 통해서 주입받는 방법
//	public UserDAO(ConnectionMaker connectionMaker) {
////		simpleConnectionMaker = new SimpleConnectionMaker();
////		1) connectionMaker = new DConnectionMaker(); => 런타임 시의 의존관계가 이미 코드속에 다 결정되어 있음
//		
//		/*
//		 * // * )의존관계 검색을 이용하는 경우 AnnotationConfigApplicationContext context = new
//		 * AnnotationConfigApplicationContext(DaoFactory.class); this.connectionMaker =
//		 * context.getBean("connectionMaker", ConnectionMaker.class);
//		 */
//		this.connectionMaker = connectionMaker;
//	}

	//수정자를 통해서 주입받는 방법
	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}
	
	//dataSource사용
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
//		Connection c = connectionMaker.makeConnection(); //인터페이스에 정의된 메소드를 사용하므로, 클래스가 바뀐다고 해도 메소드 이름이 변경되지 않음
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
