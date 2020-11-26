package com.springbook.practice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.domain.User;

public class UserDaoTest {
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
	//JUnit 테스트 메소드 적용하기
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		
		User user = new User();
		user.setId("wronggim");
		user.setName("김나영");
		user.setPassword("1234");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
}
