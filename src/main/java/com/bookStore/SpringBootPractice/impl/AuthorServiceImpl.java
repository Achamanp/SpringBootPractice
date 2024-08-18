package com.bookStore.SpringBootPractice.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.entities.*;
import com.bookStore.SpringBootPractice.exceptions.AuthorNotExistOrNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.appConstant.AuthorPageResponse;
import com.bookStore.SpringBootPractice.appConstant.BookPageResponse;
import com.bookStore.SpringBootPractice.payloads.AuthorDto;
import com.bookStore.SpringBootPractice.payloads.BookDto;
import com.bookStore.SpringBootPractice.repositories.AuthorRepository;
import com.bookStore.SpringBootPractice.repositories.BookRepository;
import com.bookStore.SpringBootPractice.service.AuthorService;
import com.twilio.exception.InvalidRequestException;
@Service
public class AuthorServiceImpl implements AuthorService{
	private AuthorRepository authorRepository;
	private ModelMapper modelMapper;
	private BookRepository bookRepository;
	public AuthorServiceImpl(AuthorRepository authorRepository,ModelMapper modelMapper,BookRepository bookRepository) {
		this.modelMapper = modelMapper;
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
	}
	@Override
	public AuthorDto createAuthor(AuthorDto authorDto) {
		if(authorDto==null) {
			throw new NullPointerException("OrderDto must not be null ");
		}
		Author author = this.modelMapper.map(authorDto, Author.class);
		Author saveAuthor = this.authorRepository.save(author);
		return this.modelMapper.map(saveAuthor, AuthorDto.class);
	}
	@Override
	public AuthorDto updateAuthor(AuthorDto authorDto,Integer id) throws AuthorNotExistOrNotFoundException {
		if(id == null) {
			throw new NullPointerException("Id must not be null");
		}
		Author author = this.authorRepository.findById(id).orElseThrow(()->
		new AuthorNotExistOrNotFoundException(" Author not found or not exist with id " + id));
		if(authorDto ==null) {
			throw new NullPointerException("AuthorDto must not be null please fill the field to update the author");
		}
		author.setName(authorDto.getName());
		Author updatedAuthor = this.authorRepository.save(author);
		return this.modelMapper.map(updatedAuthor, AuthorDto.class);
	}
	@Override
	public void deleteAuthor(Integer authorId) throws AuthorNotExistOrNotFoundException {
		if(authorId == null) {
			throw new NullPointerException("Id must not be null");
		}
		Author author = this.authorRepository.findById(authorId).orElseThrow(()->
		new AuthorNotExistOrNotFoundException(" Author not found or not exist with id " + authorId));
		this.authorRepository.delete(author);
	}
	@Override
	public AuthorDto getAuthor(Integer authorId) throws AuthorNotExistOrNotFoundException {
		if(authorId == null) {
			throw new NullPointerException("Id must not be null");
		}
		Author author = this.authorRepository.findById(authorId).orElseThrow(()->
		new AuthorNotExistOrNotFoundException(" Author not found or not exist with id " + authorId));
		return this.modelMapper.map(author, AuthorDto.class);
	}
	@Override
	public AuthorPageResponse getAllAuthor(Integer pageSize, Integer pageNumber, String sortDir, String sortBy) {
	    if (pageSize == null || pageNumber == null) {
	        throw new InvalidRequestException("Page size and page number are required");
	    }
	    if (sortDir == null || sortBy == null) {
	        throw new InvalidRequestException("Sort direction and sort by are required");
	    }
	    
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(sortDir), sortBy);
	    Page<Author> authorPage = authorRepository.findAll(pageable);
	    
	    List<AuthorDto> authorDtos = authorPage.getContent().stream()
	            .map(author -> modelMapper.map(author, AuthorDto.class))
	            .collect(Collectors.toList());
	    
	    AuthorPageResponse response = new AuthorPageResponse();
	    response.setContent(authorDtos);
	    response.setPageSize(pageSize);
	    response.setPageNumber(pageNumber);
	    response.setTotalElement(authorPage.getTotalElements());
	    response.setTotalPages(authorPage.getTotalPages());
	    
	    return response;
	}

	@Override
	public AuthorPageResponse searchAuthors(String keyword) {
	    if (keyword == null || keyword.trim().isEmpty()) {
	        throw new InvalidRequestException("Search keyword is required");
	    }
	    
	    List<Author> authors = authorRepository.searchAuthor(keyword);
	    List<AuthorDto> authorDtos = authors.stream()
	            .map(author -> modelMapper.map(author, AuthorDto.class))
	            .collect(Collectors.toList());
	    
	    AuthorPageResponse response = new AuthorPageResponse();
	    response.setContent(authorDtos);
	    
	    return response;
	}

	@Override
	public void addBookToAuthor(Integer authorId, Integer bookId) throws BookNotFoundException, AuthorNotExistOrNotFoundException {
	    if (authorId == null || bookId == null) {
	        throw new InvalidRequestException("Author ID and book ID are required");
	    }
	    
	    Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotExistOrNotFoundException("Author not found with ID " + authorId));
	    Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException("Book not found with ID " + bookId));
	    
	    author.getBooks().add(book);
	    authorRepository.save(author);
	}

	@Override
	public void removeBookFromAuthor(Integer authorId, Integer bookId) throws BookNotFoundException, AuthorNotExistOrNotFoundException {
	    if (authorId == null || bookId == null) {
	        throw new InvalidRequestException("Author ID and book ID are required");
	    }
	    
	    Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotExistOrNotFoundException("Author not found with ID " + authorId));
	    Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found with ID " + bookId));
	    
	    author.getBooks().remove(book);
	    authorRepository.save(author);
	}

	@Override
	public BookPageResponse getBooksByAuthor(Integer authorId, Integer pageSize, Integer pageNumber, String sortDir, String sortBy) throws AuthorNotExistOrNotFoundException {
	    if (authorId == null) {
	        throw new InvalidRequestException("Author ID is required");
	    }
	    if (pageSize == null || pageNumber == null) {
	        throw new InvalidRequestException("Page size and page number are required");
	    }
	    if (sortDir == null || sortBy == null) {
	        throw new InvalidRequestException("Sort direction and sort by are required");
	    }
	    
	    Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotExistOrNotFoundException("Author not found with ID " + authorId));
	    
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(sortDir), sortBy);
	    Page<Book> bookPage = bookRepository.findByAuthors(author, pageable);
	    List<Book> books = bookPage.getContent();
	    List<BookDto> bookDto = books.stream().map(dto-> this.modelMapper.map(dto, BookDto.class)).collect(Collectors.toList());
	    BookPageResponse response = new BookPageResponse();
	    response.setContent(bookDto);
	    response.setPageSize(pageSize);
	    response.setPageNumber(pageNumber);
	    response.setTotalElement(bookPage.getTotalElements());
	    response.setTotalPages(bookPage.getTotalPages());
	    
	    return response;
	}
}
