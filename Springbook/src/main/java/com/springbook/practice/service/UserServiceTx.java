package com.springbook.practice.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.springbook.practice.domain.User;

public class UserServiceTx implements UserService {

	UserService userService; //타깃 오브젝트
	
	PlatformTransactionManager transactionManager;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public void add(User user) {
		this.userService.add(user); //메소드 구현과 위임
	}

	@Override
	public void upgradeLevels() { //메소드 구현
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition()); //30-31:부가기능 수행
		try {
			
			userService.upgradeLevels(); //위임
			
			this.transactionManager.commit(status); //35-38: 부가기능 수행
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}

}
