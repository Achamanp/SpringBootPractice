package com.bookStore.SpringBootPractice.exceptions;

public class AuthorNotExistOrNotFoundException extends Exception {
	public AuthorNotExistOrNotFoundException(String message) {
		super(message);
	}
}
