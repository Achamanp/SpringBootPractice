package com.bookStore.SpringBootPractice.exceptions;

public class EmailAlreadyAssociatedException extends RuntimeException{
       public EmailAlreadyAssociatedException(String message) {
    	   super(message);
       }
}
