package com.bookStore.SpringBootPractice.exceptions;

public class BookNotFoundException extends Exception{
	public BookNotFoundException(String message) {
		super(message);
	}

}
