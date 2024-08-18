package com.bookStore.SpringBootPractice.payloads;

import java.time.LocalDateTime;

public class ReviewDto {
	    private Integer id;
	    private String content;
	    private Integer rating;
	    private LocalDateTime createdAt;
	    private BookDto book;
	    private UserDto user;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Integer getRating() {
			return rating;
		}
		public void setRating(Integer rating) {
			this.rating = rating;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
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
