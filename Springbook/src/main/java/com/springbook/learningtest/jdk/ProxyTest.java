package com.springbook.learningtest.jdk;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ProxyTest {

	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget(); //타겟은 인터페이스를 통해 접근한다
		assertThat(hello.sayHello("nyk"), is("Hello nyk"));
		assertThat(hello.sayHi("nyk"), is("Hi nyk"));
		assertThat(hello.sayThankYou("nyk"), is("Thank You nyk"));
	}
	
	@Test
	public void helloUppercaseTest() {
		Hello proxiedHello = new HelloUppercase(new HelloTarget()); //프록시를 거쳐서 타깃 오브젝트에 접근하도록 한다.
		assertThat(proxiedHello.sayHello("nyk"), is("HELLO NYK"));
		assertThat(proxiedHello.sayHi("nyk"), is("HI NYK"));
		assertThat(proxiedHello.sayThankYou("nyk"), is("THANK YOU NYK"));
	}
}
