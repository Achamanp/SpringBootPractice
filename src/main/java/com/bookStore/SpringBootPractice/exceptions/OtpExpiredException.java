package com.bookStore.SpringBootPractice.exceptions;

public class OtpExpiredException extends RuntimeException {
	public OtpExpiredException(String message) {
		super(message);
	}

}
