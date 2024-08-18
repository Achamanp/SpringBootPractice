package com.bookStore.SpringBootPractice.exceptions;

public class OrderNotFoundException extends Exception{
	public OrderNotFoundException(String message) {
		super(message);
	}
}
