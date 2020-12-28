package com.springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calcuator {
	public Integer calcSum(String filepath) throws IOException {
		LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
			@Override
			public Integer doSthWithLine(String line, Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath, sumCallback, 0);
	}
	public Integer calMultiply(String filepath) throws IOException {
		LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
			@Override
			public Integer doSthWithLine(String line, Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath, multiplyCallback, 0);
	}
	
	public String concatenate(String filepath) throws IOException {
		LineCallback<String> conCallback = new LineCallback<String>() {
			
			@Override
			public String doSthWithLine(String line, String value) {
				return value + line;
			}
		};
		return lineReadTemplate(filepath, conCallback, "");
	}
	
	public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			int ret = callback.doSthWithReader(br);
			return ret;
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
			throw e;
		} 
		finally {
			if(br != null) {
				try {br.close();} 
				catch(IOException e) {System.out.println(e.getMessage());}
			}
		}
	}
																			//계산 결과를 저장할 변수의 초기값
	public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filepath));
			T res = initVal;
			String line = null;
			while((line = br.readLine()) != null) {
				res = callback.doSthWithLine(line, res);
			}
			return res;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e;
		} 
		finally {
			if(br != null) {
				try {br.close();} 
				catch(IOException e) {System.out.println(e.getMessage());}
			}
		}
	}
}
