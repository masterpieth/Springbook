package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//���ø� �޼ҵ� ������ �̿��� context �и�
public class UserDAODeleteAll extends UserDAO{

	@Override
	protected PreparedStatement makeStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("delete from users");
		return ps;
	}
	
}
