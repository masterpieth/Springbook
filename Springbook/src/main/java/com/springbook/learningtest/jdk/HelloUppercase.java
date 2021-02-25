package com.springbook.learningtest.jdk;

public class HelloUppercase implements Hello{

	Hello hello; //다른 프록시가 추가될 수 있으므로 인터페이스로 접근한다.
	
	public HelloUppercase(Hello hello) {
		this.hello = hello;
	}
	
	@Override
	public String sayHello(String name) {
		return hello.sayHello(name).toUpperCase(); //위임, 부가기능 적용
	}

	@Override
	public String sayHi(String name) {
		return hello.sayHi(name).toUpperCase();
	}

	@Override
	public String sayThankYou(String name) {
		return hello.sayThankYou(name).toUpperCase();
	}

}
