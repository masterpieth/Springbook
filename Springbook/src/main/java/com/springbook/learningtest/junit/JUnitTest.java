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
	
	//�ϳ��� ������ ������ ��쿡�� ������ ������ ������Ʈ���� ���ϱ� ������ �������� ����.
//	static JUnitTest testObj;
	static Set<JUnitTest> testobjs = new HashSet<JUnitTest>();
	static ApplicationContext contextObj = null;
	
	//JUnit�� ������ �׽�Ʈ ������Ʈ�� �Ź� ���� �ϴ°ɱ�? �� ���� �׽�Ʈ
	@Test 
	public void test1() {
						//���� �ʾƾ� ������
//		assertThat(this, is(not(sameInstance(testObj))));
//		testObj = this;
		assertThat(testobjs, not(hasItem(this)));
		testobjs.add(this);
		
		//ù �׽�Ʈ������ null, ù �׽�Ʈ���� ������� ���ؽ�Ʈ�� ������ ������
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
