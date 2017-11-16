package com.demo.test;

import com.demo.dao.Dao;
import com.demo.dao.Entity;
import com.demo.domain.User;

public class Test {
public static void main(String[] args) {
	User user  = new User();
	user.setName("LALALANA");
	user.setPassword("123");
	user.setAddress("adhome");
	System.out.println(user.getName());
	Dao dao = new Dao();

		dao.insert((Entity) user);
	
		
	
}
}
