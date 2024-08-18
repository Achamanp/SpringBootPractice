package com.bookStore.SpringBootPractice.payloads;


public class WishlistDto {
    private Integer id;
    private BookDto book;
    private UserDto user;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BookDto getBook() {
		return book;
	}
	public void setBook(BookDto book) {
		this.book = book;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}


}
