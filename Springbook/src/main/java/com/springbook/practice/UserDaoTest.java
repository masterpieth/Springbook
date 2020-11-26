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
	//JUnit �׽�Ʈ �޼ҵ� �����ϱ�
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		
		User user = new User();
		user.setId("wronggim");
		user.setName("�質��");
		user.setPassword("1234");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
}
