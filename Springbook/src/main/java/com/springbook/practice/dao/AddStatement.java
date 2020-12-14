package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.springbook.practice.domain.User;

public class AddStatement implements StatementStrategy{
	
	User user;
	
	public AddStatement(User user) {
		this.user = user;
	}

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getId());
		ps.setString(3, user.getId());
		return null;
	}
	
}
