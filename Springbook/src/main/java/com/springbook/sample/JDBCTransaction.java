package com.springbook.sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

//필기용
public class JDBCTransaction {

	public static void main(String[] args) throws SQLException {
		
		DataSource dataSource = new SingleConnectionDataSource();
		
		Connection c = dataSource.getConnection(); //db 커넥션
		c.setAutoCommit(false); //트랜잭션 시작
		
		try {
			/* 하나의 트랜잭션으로 묶인 단위작업 시작*/
			PreparedStatement st1 = c.prepareStatement("sql cmd");
			st1.executeUpdate();
			
			PreparedStatement st2 = c.prepareStatement("sql cmd");
			st2.executeUpdate();
			/* 하나의 트랜잭션으로 묶인 단위작업 끝*/
			
			c.commit(); //트랜잭션 커밋
		} catch (Exception e) {
			c.rollback(); //트랜잭션 롤백
		}
		c.close();
	}
}
