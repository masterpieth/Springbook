package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.mysql.jdbc.MysqlErrorNumbers;
import com.springbook.exception.DuplicateUserIdException;
import com.springbook.practice.domain.User;

public abstract class UserDAOBackup {
//	private SimpleConnectionMaker simpleConnectionMaker;
	private ConnectionMaker connectionMaker; //인터페이스를 통해 오브젝트에 접근하므로 구체적 클래스 정보를 알 필요가 없다.
	private Connection c;
	private User user;
	
	//datasource 사용
//	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
//	private JdbcContext jdbcContext;
	
//	public void setJdbcContext(JdbcContext jdbcContext) {
//		this.jdbcContext = jdbcContext;
//	}
	
	private RowMapper<User> userMapper = new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};
	
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
	//201217 JdbcContext 생성, DI를 동시에 수행하게 만듦
	public void setDataSource(DataSource dataSource) {
//		this.jdbcContext = new JdbcContext();
//		this.jdbcContext.setDataSource(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
//		this.dataSource = dataSource;
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
		//DI받은 컨텍스트 메소드를 사용함->템플릿
//		this.jdbcContext.workWithStatementStrategy(
//				//콜백 오브젝트 생성->템플릿에 매개로 넘겨줌(콜백의 메소드를 실행함)
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
//		jdbcTemplate사용하는 방법
		this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", 
				user.getId(), user.getName(), user.getPassword()); 
	}
	
	//예외 전환 사례
//	public void add(User user) throws DuplicateUserIdException, SQLException {
//		try {
//		} catch(SQLException e) {
//			// ErrorCode가 mysql의 duplicate Entry(1062)면 예외전환
//			if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) throw DuplicateUserIdException();
//			else throw e; //그 외의 경우는 그냥 던짐
//		}
//	}
	//예외 만들어서 적용
//	public void add2(User user) throws DuplicateUserIdException {
//		try {
//			//JDBC를 이용해 정보를 추가하는 코드 또는 SQL예외 발생하는 코드
//		} catch(SQLException e) {
//			if(e.getErrorCo-de() == MysqlErrorNumbers.ER_DUP_ENTRY) throw new DuplicateUserIdException(e);
//			else throw new RuntimeException(e);
//		}
//	}
	
	public void add() throws DuplicateUserIdException {
		try {
			//JDBC를 이용해 정보를 추가하는 코드
		}
		catch(DuplicateKeyException e) {
			//로그찍기 등
			throw new DuplicateUserIdException(e); //원인이 되는 예외 중첩해서 던짐
		}
	}
//	public User get(String id) throws ClassNotFoundException, SQLException{
	public User get(String id) {
		
//		Connection c = connectionMaker.makeConnection();
//		this.c = connectionMaker.makeConnection();
//		Connection c = dataSource.getConnection();
//		
//		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//		ps.setString(1, id);
//		
//		ResultSet rs = ps.executeQuery();
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
//		User user = null;
//		if(rs.next()) {
//			//결과가 있으면 값을 세팅함
//			user = new User();
//			user.setId(rs.getString("id"));
//			user.setName(rs.getString("name"));
//			user.setPassword(rs.getString("password"));
//		}
//		
//		rs.close();
//		ps.close();
//		c.close();
//		
//		//결과가 없으면 예외를 발생시킨다.
//		if(user == null) throw new EmptyResultDataAccessException(1);
//		
//		return user;
		//rowMapper를 사용한 콜백 활용
		return this.jdbcTemplate.queryForObject("select * from users where id=?", 
				new Object[] {id}, //SQL에 바인딩한 파라미터 값, 가변인자 대신 배열을 사용함
				this.userMapper
//				new RowMapper<User>() { //RowMapper 콜백
//					public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//						User user = new User();
//						user.setId(rs.getString("id"));
//						user.setName(rs.getString("name"));
//						user.setPassword(rs.getString("password"));
//						return user;
//				}
//		});
		);
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
//		this.jdbcContext.workWithStatementStrategy(
//			new StatementStrategy() {
//				public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//					//sql 문장 외에는 변하지 않음
//					return c.prepareStatement("delete from users");
//				}
//			}
//		);
		
		//고정되어있는 부분을 메소드로 분리함
//		executeSql("delete from users");
		//컨텍스트로 옮긴 콜백사용
//		this.jdbcContext.executeSql("delete from users");
		
		//jdbcTemplate사용 1) 콜백 패턴 사용하는 메소드
//		this.jdbcTemplate.update(new PreparedStatementCreator() {
//			
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("delete from users");
//			}
//		});
//		2) sql문만 사용하는 메소드
		this.jdbcTemplate.update("delete from users");
	}
//	public void jdbcContextWithStatmentStrategy(StatementStrategy stmt) throws SQLException{
//		Connection c = null;
//		PreparedStatement ps = null;
//		
//		try {
//			c = dataSource.getConnection();
//			ps = stmt.makePreparedStatement(c);
//			ps.executeUpdate();
//		} catch (SQLException e) {
//			throw e;
//		} finally {
//			if(ps != null) {
//				try {
//					ps.close();
//				} catch (SQLException e) {
//				}
//			}
//			if(c != null) {
//				try {
//					c.close();
//				} catch (SQLException e) {
//				}
//			}
//		}
//	}
	public int getCount() throws SQLException {
//		Connection c = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			c = dataSource.getConnection();
//			ps = c.prepareStatement("select count(*) from users");
//			
//			//resultset에서도 sqlexception이 발생할 수 있으므로, try 블록 안에 있어야함
//			rs = ps.executeQuery();
//			rs.next();
//			return rs.getInt(1);
//			
//		} catch (SQLException e) {
//			throw e;
//		} finally {
//			//resultset에 대한 예외처리
//			if(rs != null) {
//				try {
//					rs.close(); //close()의 경우, 만들어진순서의 반대로 하는 것이 원칙
//				} catch (SQLException e) {
//				}
//			}
//			if(ps != null) {
//				try {
//					ps.close();
//				} catch (SQLException e) {
//				}
//			}
//			if(c != null) {
//				try {
//					c.close();
//				} catch (SQLException e) {
//				}
//			}
//		}
//		jdbcTemplate 사용 1) 콜백 패턴 메소드
//		return this.jdbcTemplate.query(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("select count(*) from users");
//			}
//		}, new ResultSetExtractor<Integer>() {
//			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//				rs.next();
//				return rs.getInt(1);
//			}
//		});
//		2) query만 사용하는 메소드
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", 
				this.userMapper
//			new RowMapper<User>() {
//				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//					User user = new User();
//					user.setId(rs.getString("id"));
//					user.setName(rs.getString("name"));
//					user.setPassword(rs.getString("password"));
//					return user;
//				}
//		});
		);
	}
//	private void executeSql(final String query) throws SQLException {
//		this.jdbcContext.workWithStatementStrategy(
//				new StatementStrategy() {
//					public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//						return c.prepareStatement(query);
//					}
//				}
//			);
//	}
	abstract protected PreparedStatement makeStatement(Connection c) throws SQLException ;
}
