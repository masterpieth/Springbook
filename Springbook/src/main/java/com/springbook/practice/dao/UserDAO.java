package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import com.springbook.practice.domain.User;

public abstract class UserDAO {
	
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
//		Connection c = dataSource.getConnection();
//		
//		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
//		ps.setString(1, user.getId());
//		ps.setString(2, user.getName());
//		ps.setString(3, user.getPassword());
//		
//		ps.executeUpdate();
//		
//		ps.close();
//		c.close();
		
		//전략 패턴을 사용한 메소드 분리
		StatementStrategy st = new AddStatement(user);
		jdbcContextWithStatmentStrategy(st);
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
		
//		Connection c = connectionMaker.makeConnection();
//		this.c = connectionMaker.makeConnection();
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
//		rs.next();
//		User user = new User();
//		user.setId(rs.getString("id"));
//		user.setName(rs.getString("name"));
//		user.setPassword(rs.getString("password"));
		
//		this.user = new User();
//		this.user.setId(rs.getString("id"));
//		this.user.setName(rs.getString("name"));
//		this.user.setPassword(rs.getString("password"));
		
		//값이 없는 경우를 위한 예외처리
		User user = null;
		if(rs.next()) {
			//결과가 있으면 값을 세팅함
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		//결과가 없으면 예외를 발생시킨다.
		if(user == null) throw new EmptyResultDataAccessException(1);
		
		return user;
	}
	
	public void deleteAll() throws SQLException {
		StatementStrategy st = new DeleteAllStatement();
		jdbcContextWithStatmentStrategy(st);
	}
	public void jdbcContextWithStatmentStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	public int getCount() throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			
			//resultset에서도 sqlexception이 발생할 수 있으므로, try 블록 안에 있어야함
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			//resultset에 대한 예외처리
			if(rs != null) {
				try {
					rs.close(); //close()의 경우, 만들어진순서의 반대로 하는 것이 원칙
				} catch (SQLException e) {
				}
			}
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	abstract protected PreparedStatement makeStatement(Connection c) throws SQLException ;
}

