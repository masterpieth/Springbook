package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//전략 패턴을 사용한 기능 분리. 인터페이스(전략) 구현 클래스
public class DeleteAllStatement implements StatementStrategy{

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("delete from users");
		return ps;
	}

}
