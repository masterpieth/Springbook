package com.springbook.practice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springbook.practice.dao.UserDAOJdbc;
import com.springbook.practice.domain.User;

//@ContextConfiguration(locations = "/applicationContext.xml") //테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
//@DirtiesContext // 테스트 메소드에서 애플리케이션 컨텍스트의 구성이나 상태를 변경한다는 것을 컨텍스트 프레임워크에 알려줌
@RunWith(SpringJUnit4ClassRunner.class) //스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
//@ContextConfiguration(locations = "/test-applicationContext.xml") //테스트시에는 테스트용 설정파일을 사용함
public class UserDaoTestBackup {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//userdao가 사용할 connectionmaker 구현 클래스를 결정하고 오브젝트를 만든다.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		//dao에게 넘겨줌으로써 두 오브젝트 사이의 의존관계가 설정된다.
//		UserDAO dao = new UserDAO(connectionMaker);
		//위 작업은 userDao와 connectionMaker 구현 클래스와의 런타임 오브젝트 의존관계를 설정하는 책임을 클라이언트로 가져온 결과이다.
		
		//팩토리를 사용해서 dao를 받아서 사용함
//		UserDAO dao = new DaoFactory().userDAO();
		
		
		//DaoFactory를 설정정보로 사용하는 애플리케이션 컨텍스트
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//applicationContext.xml을 설정정보로 사용함
		//classpath -> build path에서 확인 src/main/resource에 넣어줌
		
		
		//org/springframework/core/env/EnvironmentCapable -> spring core 설치 안되어있어서, 혹은 버전이 맞지 않음 -> classnotFound는 대체로 다 라이브러리의 문제
		//클래스 찾지 못하는 문제 -> classpath : src/main/java 혹은 src/main/resources -> 아래에 위치시킴
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml", 클래스패스 힌트가 되는 클래스.class); 클래스의 위치를 바탕으로 xml의 위치를 추정함. 보통은 위에거씀
		
		//첫번째 파라미터: cpplicationcontext에 등록된 빈의 이름(메소드 이름)
//		UserDAO dao = context.getBean("userDAO", UserDAO.class);
//		
//		User user = new User();
//		user.setId("wronggim");
//		user.setName("김나영");
//		user.setPassword("1234");
//		
//		dao.add(user);
//		
//		System.out.println(user.getId() + "등록 성공");
//		
//		User user2 = dao.get(user.getId());
		//get()메소드가 에러가 없었을 뿐, 원하는 결과였는지 테스트 하는 코드는 아니었음
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//		System.out.println(user2.getId() + "조회 성공");
		
//		if(!user.getName().equals(user2.getName())) {
//			System.out.println("테스트 실패(name)");
//		}
//		else if (!user.getPassword().equals(user2.getPassword())) {
//			System.out.println("테스트 실패(password)");
//		}
//		else {
//			System.out.println("테스트 성공");
//		}
		
		JUnitCore.main("com.springbook.practice.UserDaoTest");
	}
	
//	@Autowired //컨테이너 없이 DI 테스트를 하기 위해서 주석처리됨
	private UserDAOJdbc dao;
	private User user1;
	private User user2;
	private User user3;
	
	/*
	 * 어떤 빈이든 다 가져올 수 있고, 할당 가능한 타입을 가진 빈을 자동으로 찾기 때문에 클래스, 인터페이스 타입 모두 가능함
	 * @Autowired
	 * SimpleDriverDataSource dataSource;
	 */
//	@Autowired
//	private ApplicationContext context; //테스트 오브젝트가 만들어지면 해당 필드에 자동으로 주입된다.
	
	//JUnit 테스트 메소드 적용하기
	
	//반복되는 작업을 별도의 메소드로 옮김
	@Before
	public void setUp() {
//		this.dao = context.getBean("userDAO", UserDAO.class);
		this.user1 = new User("wronggim1", "nyk1", "1234");
		this.user2 = new User("wronggim2", "nyk2", "1234");
		this.user3 = new User("wronggim3", "nyk3", "1234");
		
		//테스트에서 userdao가 사용할 오브젝트를 직접 생성함(비추)
//		DataSource dataSource= new SingleConnectionDataSource("jdbc:mysql://localhost/testdb","root","1234", true);
		
//		dao = new UserDAO();
		DataSource dataSource= new SingleConnectionDataSource("jdbc:mysql://localhost/testdb","root","1234", true);
		dao.setDataSource(dataSource);
	}
	
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		//정말로 add된 값을 그대로 가져온것일까? 에 대한 테스트
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}
	//하나의 테스트는 하나의 기능만 검증해야한다
	@Test
	public void count() throws SQLException, ClassNotFoundException {
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	//값이 없을 때 예외가 발생하는 테스트
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		//get에 대한 예외가 발생하기 때문에, userdao에서 get을 수정해주어야 한다.
		dao.get("없는 ID");
	}
	
	@Test
	public void getAll() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		
		//예외상황에 대한 테스트 -> 데이터가 없을 때
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1, users3.get(0)); //id 순으로 들어갈 것이므로 순서대로 맞아야함
		checkSameUser(user2, users3.get(1));
		checkSameUser(user3, users3.get(2));
	}
	
	//User 오브젝트의 내용을 비교, 중복되는 메소드라 따로 분리됨
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
	}
}