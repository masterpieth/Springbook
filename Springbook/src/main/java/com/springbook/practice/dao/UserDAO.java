package com.springbook.practice.dao;

import java.util.List;

import com.springbook.practice.domain.User;

public interface UserDAO {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
}
