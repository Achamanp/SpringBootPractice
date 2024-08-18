package com.bookStore.SpringBootPractice.payloads;

import java.time.LocalDateTime;
import java.util.Date;

public class UserDto {
	private Integer Id;
	private String username;
	private String password;
	private LocalDateTime updatedAt = LocalDateTime.now();
	private LocalDateTime createdAt = LocalDateTime.now();
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	
}
