package com.springbook.practice;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.springbook.practice.dao.ConnectionMaker;
import com.springbook.practice.dao.DConnectionMaker;
import com.springbook.practice.dao.DaoFactory;
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
		ApplicationContext context = new GenericXmlApplicationContext("/WEB-INF/spring/appServlet/applicationContext.xml");
		
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml", Ŭ�����н� ��Ʈ�� �Ǵ� Ŭ����.class); Ŭ������ ��ġ�� �������� xml�� ��ġ�� ������. ������ �����ž�
		
		//ù��° �Ķ����: cpplicationcontext�� ��ϵ� ���� �̸�(�޼ҵ� �̸�)
		UserDAO dao = context.getBean("userDAO", UserDAO.class);
		
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
