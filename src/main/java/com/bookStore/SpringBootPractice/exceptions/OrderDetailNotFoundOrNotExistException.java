package com.bookStore.SpringBootPractice.exceptions;

public class OrderDetailNotFoundOrNotExistException extends Exception{
public OrderDetailNotFoundOrNotExistException(String message) {
	super(message);
}
}
