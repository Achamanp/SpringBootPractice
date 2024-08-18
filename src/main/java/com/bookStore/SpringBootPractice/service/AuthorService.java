package com.bookStore.SpringBootPractice.service;

import com.bookStore.SpringBootPractice.appConstant.AuthorPageResponse;
import com.bookStore.SpringBootPractice.appConstant.BookPageResponse;
import com.bookStore.SpringBootPractice.exceptions.AuthorNotExistOrNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.payloads.AuthorDto;

public interface AuthorService {

	    AuthorDto createAuthor(AuthorDto authorDto) ;
	    AuthorDto updateAuthor(AuthorDto authorDto,Integer id) throws AuthorNotExistOrNotFoundException;
	    void deleteAuthor(Integer authorId) throws AuthorNotExistOrNotFoundException;
	    AuthorDto getAuthor(Integer authorId) throws AuthorNotExistOrNotFoundException;
	    AuthorPageResponse getAllAuthor(Integer pageSize,Integer pageNumber, String sortDir, String sortBy);
	    AuthorPageResponse searchAuthors(String keyword);
	    void addBookToAuthor(Integer authorId, Integer bookId) throws BookNotFoundException, AuthorNotExistOrNotFoundException;
	    void removeBookFromAuthor(Integer authorId, Integer bookId) throws BookNotFoundException, AuthorNotExistOrNotFoundException;
	    BookPageResponse getBooksByAuthor(Integer authorId,Integer pageSize,Integer pageNumber, String sortDir, String sortBy) throws AuthorNotExistOrNotFoundException;
	
}
