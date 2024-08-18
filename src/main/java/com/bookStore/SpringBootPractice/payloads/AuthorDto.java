package com.bookStore.SpringBootPractice.payloads;

import java.util.Set;
public class AuthorDto {
	private Integer id;
	private Set<BookDto> books;
	    private String name;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Set<BookDto> getBooks() {
			return books;
		}
		public void setBooks(Set<BookDto> books) {
			this.books = books;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

}
