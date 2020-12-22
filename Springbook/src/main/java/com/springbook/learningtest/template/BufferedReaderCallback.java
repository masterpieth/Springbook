package com.springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderCallback {
	Integer doSthWithReader(BufferedReader br) throws IOException;
}
