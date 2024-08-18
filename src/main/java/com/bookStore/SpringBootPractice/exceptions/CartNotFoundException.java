package com.bookStore.SpringBootPractice.exceptions;

public class CartNotFoundException extends Exception{
	public CartNotFoundException(String message) {
		super(message);
	}

}
