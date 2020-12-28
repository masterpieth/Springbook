package com.springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {

	Calcuator calculator;
	String numFilepath;
	
	@Before
	public void setUp() {
		this.calculator = new Calcuator();
		this.numFilepath = getClass().getResource("/numbers.txt").getPath();
	}
	@Test
	public void sumOfNumbers() throws IOException {
		assertThat(calculator.calcSum(this.numFilepath), is(10));
	}
	@Test
	public void multiplyOfNumbers() throws IOException {
		assertThat(calculator.calMultiply(this.numFilepath), is(24));
	}
}
