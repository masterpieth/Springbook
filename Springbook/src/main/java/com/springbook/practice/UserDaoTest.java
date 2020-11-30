package com.springbook.practice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.domain.User;

public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//userdao�� ����� connectionmaker ���� Ŭ������ �����ϰ� ������Ʈ�� �����.
//		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		//dao���� �Ѱ������ν� �� ������Ʈ ������ �������谡 �����ȴ�.
//		UserDAO dao = new UserDAO(connectionMaker);
		//�� �۾��� userDao�� connectionMaker ���� Ŭ�������� ��Ÿ�� ������Ʈ �������踦 �����ϴ� å���� Ŭ���̾�Ʈ�� ������ ����̴�.
		
		//���丮�� ����ؼ� dao�� �޾Ƽ� �����
//		UserDAO dao = new DaoFactory().userDAO();
		
		
		//DaoFactory�� ���������� ����ϴ� ���ø����̼� ���ؽ�Ʈ
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		//applicationContext.xml�� ���������� �����
		//classpath -> build path���� Ȯ�� src/main/resource�� �־���
		
		
		//org/springframework/core/env/EnvironmentCapable -> spring core ��ġ �ȵǾ��־, Ȥ�� ������ ���� ���� -> classnotFound�� ��ü�� �� ���̺귯���� ����
		//Ŭ���� ã�� ���ϴ� ���� -> classpath : src/main/java Ȥ�� src/main/resources -> �Ʒ��� ��ġ��Ŵ
//		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml", Ŭ�����н� ��Ʈ�� �Ǵ� Ŭ����.class); Ŭ������ ��ġ�� �������� xml�� ��ġ�� ������. ������ �����ž�
		
		//ù��° �Ķ����: cpplicationcontext�� ��ϵ� ���� �̸�(�޼ҵ� �̸�)
//		UserDAO dao = context.getBean("userDAO", UserDAO.class);
//		
//		User user = new User();
//		user.setId("wronggim");
//		user.setName("�質��");
//		user.setPassword("1234");
//		
//		dao.add(user);
//		
//		System.out.println(user.getId() + "��� ����");
//		
//		User user2 = dao.get(user.getId());
		//get()�޼ҵ尡 ������ ������ ��, ���ϴ� ��������� �׽�Ʈ �ϴ� �ڵ�� �ƴϾ���
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//		System.out.println(user2.getId() + "��ȸ ����");
		
//		if(!user.getName().equals(user2.getName())) {
//			System.out.println("�׽�Ʈ ����(name)");
//		}
//		else if (!user.getPassword().equals(user2.getPassword())) {
//			System.out.println("�׽�Ʈ ����(password)");
//		}
//		else {
//			System.out.println("�׽�Ʈ ����");
//		}
		
		JUnitCore.main("com.springbook.practice.UserDaoTest");
	}
	
	private UserDAO dao;
	private User user1;
	private User user2;
	private User user3;
	
	//JUnit �׽�Ʈ �޼ҵ� �����ϱ�
	
	//�ݺ��Ǵ� �۾��� ������ �޼ҵ�� �ű�
	@Before
	public void setUp() {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		this.dao = context.getBean("userDAO", UserDAO.class);
		
		this.user1 = new User("wronggim1", "nyk1", "1234");
		this.user2 = new User("wronggim2", "nyk2", "1234");
		this.user3 = new User("wronggim3", "nyk3", "1234");
	}
	
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		//������ add�� ���� �״�� �����°��ϱ�? �� ���� �׽�Ʈ
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}
	//�ϳ��� �׽�Ʈ�� �ϳ��� ��ɸ� �����ؾ��Ѵ�
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
	//���� ���� �� ���ܰ� �߻��ϴ� �׽�Ʈ
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		//get�� ���� ���ܰ� �߻��ϱ� ������, userdao���� get�� �������־�� �Ѵ�.
		dao.get("���� ID");
	}
}
