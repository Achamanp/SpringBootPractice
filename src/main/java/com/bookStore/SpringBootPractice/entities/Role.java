package com.bookStore.SpringBootPractice.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
@Entity
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
   private Integer id;
   private String name;
   @ManyToMany(mappedBy = "role")
   private Set<User>user = new HashSet<User>();
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Set<User> getUser() {
	return user;
}
public void setUser(Set<User> user) {
	this.user = user;
}
public Role(String name) {
	super();
	this.name = name;
}
public Role() {
	super();
	// TODO Auto-generated constructor stub
}
   
}
