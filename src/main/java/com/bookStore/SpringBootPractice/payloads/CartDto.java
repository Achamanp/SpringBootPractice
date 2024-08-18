package com.bookStore.SpringBootPractice.payloads;

import java.util.Set;



public class CartDto {
	private Integer id;
	private UserDto user;
	private Set<CartItemDto> item;
	private double totalAmount;
	private boolean savedForLater;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public Set<CartItemDto> getItem() {
		return item;
	}
	public void setItem(Set<CartItemDto> item) {
		this.item = item;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public boolean isSavedForLater() {
		return savedForLater;
	}
	public void setSavedForLater(boolean savedForLater) {
		this.savedForLater = savedForLater;
	}
	
}
