package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//���� ������ ����� ��� �и�. �������̽�(����) ���� Ŭ����
public class DeleteAllStatement implements StatementStrategy{

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("delete from users");
		return ps;
	}

}
