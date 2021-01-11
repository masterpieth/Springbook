package com.springbook.practice.domain;

public enum Level {
	BASIC(1), SILVER(2), GOLD(3); //이늄 오브젝트 정의
	
	private final int value;
	
	Level(int value) { //db에 저장할 값을 넣어줄 생성자 만듦
		this.value = value;
	}
	
	public int intValue() { //value 가져오는 메소드
		return value;
	}
	
	public static Level valueOf(int value) { // 값으로부터 level타입 오브젝트를 가져오도록 함
		switch(value) {
		case 1: return BASIC;
		case 2: return SILVER;
		case 3: return GOLD;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}

