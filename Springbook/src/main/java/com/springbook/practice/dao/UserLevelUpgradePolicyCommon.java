package com.springbook.practice.dao;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.springbook.practice.domain.Level;
import com.springbook.practice.domain.User;

public class UserLevelUpgradePolicyCommon implements UserLevelUpgradePolicy{

	UserDAO userDAO;
	
	public static final int MIN_LOGOUT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
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
		
		Properties props = new Properties();
		
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		
		Authenticator auth = new MailAuth();
		
		Session s = Session.getInstance(props, auth);
		MimeMessage message = new MimeMessage(s);
		try {
			message.setFrom(new InternetAddress("useradmin@ksug.org"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("Upgrade 안내");
			message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");
			
			Transport.send(message);
		} catch (AddressException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
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
