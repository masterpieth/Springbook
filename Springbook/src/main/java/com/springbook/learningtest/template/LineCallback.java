package com.springbook.learningtest.template;

public interface LineCallback <T>{
	T doSthWithLine(String line, T value);
}
