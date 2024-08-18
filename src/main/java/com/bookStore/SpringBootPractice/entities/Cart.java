package com.bookStore.SpringBootPractice.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private User user;
	@OneToMany(mappedBy = "cart")
	private Set<CartItem> item;
	private double totalAmount;
	private boolean savedForLater;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Set<CartItem> getItem() {
		return item;
	}
	public void setItem(Set<CartItem> item) {
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
