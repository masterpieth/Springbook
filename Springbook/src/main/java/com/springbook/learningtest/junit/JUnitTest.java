package com.springbook.learningtest.junit;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JUnitTest {
	
	@Autowired
	ApplicationContext context;
	
	//하나만 가지고 돌리는 경우에는 직전에 생성된 오브젝트랑만 비교하기 때문에 적절하지 않음.
//	static JUnitTest testObj;
	static Set<JUnitTest> testobjs = new HashSet<JUnitTest>();
	static ApplicationContext contextObj = null;
	
	//JUnit은 정말로 테스트 오브젝트를 매번 만들어서 하는걸까? 에 대한 테스트
	@Test 
	public void test1() {
						//같지 않아야 성공함
//		assertThat(this, is(not(sameInstance(testObj))));
//		testObj = this;
		assertThat(testobjs, not(hasItem(this)));
		testobjs.add(this);
		
		//첫 테스트에서는 null, 첫 테스트에서 만들어진 컨텍스트를 변수에 저장함
		assertThat(contextObj == null || contextObj == this.context, is(true));
		contextObj = this.context;
	}
	@Test 
	public void test2() {
//		assertThat(this, is(not(sameInstance(testObj))));
//		testObj = this;
		assertThat(testobjs, not(hasItem(this)));
		testobjs.add(this);
		
		assertTrue(contextObj == null || contextObj == this.context);
		contextObj = this.context;
	}
	@Test 
	public void test3() {
//		assertThat(this, is(not(sameInstance(testObj))));
//		testObj = this;
		assertThat(testobjs, not(hasItem(this)));
		testobjs.add(this);
		
//		assertThat(contextObj, 
//				either(is(nullValue())).or(is(this.context)));
		contextObj = this.context;
	}
}
