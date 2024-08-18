package com.bookStore.SpringBootPractice.exceptions;

import java.time.LocalDateTime;

public class ErrorDetail {
	public String message;
	public String description;
	public LocalDateTime localDateTime;
	public ErrorDetail(String message, String description, LocalDateTime localDateTime) {
		super();
		this.message = message;
		this.description = description;
		this.localDateTime = localDateTime;
	}
	

}
