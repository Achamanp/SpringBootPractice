package com.bookStore.SpringBootPractice.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
public class User {
	  @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Integer id;

	    @NotBlank(message = "Username cannot be blank")
	    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	    private String username;

	    @NotBlank(message = "Email cannot be blank")
	    @Email(message = "Email should be valid")
	    private String email;

	    @NotBlank(message = "Password cannot be blank")
	    @Size(min = 8, message = "Password must be at least 8 characters long")
	    private String password;

	    @OneToOne(mappedBy = "user")
	    private Cart cart;

	    @NotEmpty(message = "User must have at least one role")
	    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	    private Set<Role> role = new HashSet<>();

	    @PastOrPresent(message = "Created at must be in the past or present")
	    private LocalDateTime created_at;

	    @PastOrPresent(message = "Updated at must be in the past or present")
	    private LocalDateTime updated_at;

	    @OneToMany(mappedBy = "user")
	    private Set<Review> reviews;

	    @OneToMany(mappedBy = "user")
	    private Set<Order> order;

	    @OneToMany(mappedBy = "user")
	    private List<Wishlist> wishlists = new ArrayList<>();

	    @OneToMany(mappedBy = "user")
	    private Set<CartItem> cartItem;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Role> getRole() {
		return role;
	}
	public void setRole(Set<Role> role) {
		this.role = role;
	}
	public String getWishlist() {
		// TODO Auto-generated method stub
		return null;
	}
	public Set<Review> getReviews() {
		return reviews;
	}
	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}
	public Set<Order> getOrder() {
		return order;
	}
	public void setOrder(Set<Order> order) {
		this.order = order;
	}
	public List<Wishlist> getWishlists() {
		return wishlists;
	}
	public void setWishlists(List<Wishlist> wishlists) {
		this.wishlists = wishlists;
	}
	
	
}
