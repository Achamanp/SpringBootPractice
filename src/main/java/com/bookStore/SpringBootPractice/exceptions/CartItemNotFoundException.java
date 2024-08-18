package com.bookStore.SpringBootPractice.exceptions;

public class CartItemNotFoundException extends Exception {
	public CartItemNotFoundException(String message) {
		super(message);
	}
}
