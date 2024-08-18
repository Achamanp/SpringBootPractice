package com.bookStore.SpringBootPractice.exceptions;

public class WishlistNotFoundOrNotExistException extends Exception {
	public WishlistNotFoundOrNotExistException(String message) {
		super(message);
		
	}
}
