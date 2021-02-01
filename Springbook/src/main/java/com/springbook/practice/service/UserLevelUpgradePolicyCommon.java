package com.springbook.practice.service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.springbook.practice.dao.UserDAO;
import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;

public class UserLevelUpgradePolicyCommon implements UserLevelUpgradePolicy{

	UserDAO userDAO;
	
	private MailSender mailSender;
	
	public static final int MIN_LOGOUT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC: return (user.getLogin() >= MIN_LOGOUT_FOR_SILVER);
		case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDAO.update(user);
		sendUpgradeEmail(user);
	}
	 
	private void sendUpgradeEmail(User user) {
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("kny0339@gmail.com");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");
		
		this.mailSender.send(mailMessage);
		
	}
	
	class MailAuth extends Authenticator {
		PasswordAuthentication pwAuth;

		public MailAuth() {
			String mail_id = "";
			//원격에 올릴때 삭제하고 올리기
			String mail_pw = "";
			
			pwAuth = new PasswordAuthentication(mail_id, mail_pw);
		}
		public PasswordAuthentication getPasswordAuthentication() {
			return pwAuth;
		}
	}
}
