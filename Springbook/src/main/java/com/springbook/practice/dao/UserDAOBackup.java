package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import com.springbook.practice.domain.User;

public abstract class UserDAOBackup {
//	private SimpleConnectionMaker simpleConnectionMaker;
	private ConnectionMaker connectionMaker; //인터페이스를 통해 오브젝트에 접근하므로 구체적 클래스 정보를 알 필요가 없다.
	private Connection c;
	private User user;
	
	//datasource 사용
	private DataSource dataSource;
	
	private JdbcContext jdbcContext;
	
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
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
	
	//로컬 클래스에서 외부변수를 사용할 때는 final로 선언해줘야함
	public void add(final User user) throws ClassNotFoundException, SQLException {
		//로컬 클래스의 사용
//		class AddStatement implements StatementStrategy{
//			@Override
//			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
//				
//				//로컬 클래스로 선언되었으므로 필드에 직접 접근이 가능함-> 생성자를 통해 주입시키지 않아도 됨
//				ps.setString(1, user.getId());
//				ps.setString(2, user.getName());
//				ps.setString(3, user.getPassword());
//				return ps;
//			}
//		}
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
		//익명 클래스의 사용
//		StatementStrategy st = new StatementStrategy() {
//			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
//				ps.setString(1, user.getId());
//				ps.setString(2, user.getName());
//				ps.setString(3, user.getPassword());
//				return ps;
//			}
//		};
		//익명 클래스를 사용했기 때문에 굳이 오브젝트를 형성할 필요가 없음, 바로 메소드 내에서 사용할 수 있게 함
//		jdbcContextWithStatmentStrategy(
//			new StatementStrategy() {
//				public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//					PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
//					ps.setString(1, user.getId());
//					ps.setString(2, user.getName());
//					ps.setString(3, user.getPassword());
//					return ps;
//				}
//			}
//		);
		//DI받은 컨텍스트 메소드를 사용함
		this.jdbcContext.workWithStatementStrategy(
			new StatementStrategy() {
				public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
					PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
					ps.setString(1, user.getId());
					ps.setString(2, user.getName());
					ps.setString(3, user.getPassword());
					return ps;
				}
			}
		);
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
//		StatementStrategy st = new DeleteAllStatement();
//		jdbcContextWithStatmentStrategy(st);
		
		//익명 클래스를 사용해서 파라미터에 전달
//		jdbcContextWithStatmentStrategy(
//			new StatementStrategy() {
//				public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//					return c.prepareStatement("delete from users");
//				}
//			}
//		);
		//DI받은 컨텍스트 메소드를 사용함
		this.jdbcContext.workWithStatementStrategy(
			new StatementStrategy() {
				public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
					return c.prepareStatement("delete from users");
				}
			}
		);
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
