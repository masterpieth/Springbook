package com.springbook.practice.domain;

public enum Level {
	GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);
	
	private final int value;
	private final Level next;
	
	Level(int value, Level next) {
		this.value = value;
		this.next = next;
	}
	
	public int intValue() { //value 가져오는 메소드
		return value;
	}
	
	public Level nextLevel() {
		return this.next;
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

