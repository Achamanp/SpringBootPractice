package com.bookStore.SpringBootPractice.exceptions;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String message) {
    	super(message);
    }
}
