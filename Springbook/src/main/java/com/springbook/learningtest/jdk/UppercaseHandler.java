package com.springbook.learningtest.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler{

//Hello 단일 인터페이스에 대한 Handler
//	Hello target;
//	
//	public UppercaseHandler(Hello target) {
//		this.target = target;
//	}
//	
//	@Override
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		String ret = (String)method.invoke(target, args);
//		return ret.toUpperCase();
//	}

	//Handler 확장- 모든 오브젝트에 대한 처리가 가능하게 바꿈
	Object target;
	
	//어떤 종류의 인터페이스를 구현한 타깃에도 적용이 가능하도록 Object 타입으로 수정함
	UppercaseHandler(Object target) {
		this.target = target;
	}
	//String인 경우에만 대문자를 반환함
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		
//		Object ret = method.invoke(target, args);
//		
//		if(ret instanceof String) return ((String)ret).toUpperCase();
//		else return ret;
//	}
	
	//메소드의 이름을 바탕으로 선별해서 부가기능을 적용함
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		Object ret = method.invoke(target, args);
									//리턴 타입과 메소드 이름이 일치하는 경우에만 부가기능을 적용한다.
		if(ret instanceof String && method.getName().startsWith("say")) return ((String)ret).toUpperCase();
		else return ret; //조건이 일치하지 않으면 타깃 오브젝트의 호출 결과를 그대로 리턴한다
	}
}
