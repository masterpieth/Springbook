package com.springbook.practice.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker{

	int counter = 0;
	private ConnectionMaker realConnectionMaker;
	
	public CountingConnectionMaker(ConnectionMaker reConnectionMaker) {
		this.realConnectionMaker = reConnectionMaker;
	}

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		//makeConnectio할 때 카운터를 증가시킴
		this.counter++;
		//증가시키고 난 다음에 connectionMaker 오브젝트를 돌려줌(db 커넥션을 주긴 해야 동작하니까)
		return realConnectionMaker.makeConnection();
	}
	public int getCounter () {
		return this.counter;
	}
}
