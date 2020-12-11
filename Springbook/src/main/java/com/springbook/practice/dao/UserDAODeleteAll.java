package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//템플릿 메소드 패턴을 이용한 context 분리
public class UserDAODeleteAll extends UserDAO{

	@Override
	protected PreparedStatement makeStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("delete from users");
		return ps;
	}
	
}
