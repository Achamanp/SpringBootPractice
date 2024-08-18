package com.bookStore.SpringBootPractice.service;

import java.util.List;
import com.bookStore.SpringBootPractice.appConstant.BookPageResponse;
import com.bookStore.SpringBootPractice.exceptions.AuthorNotExistOrNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ReviewNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.BookDto;
import com.bookStore.SpringBootPractice.payloads.ReviewDto;

public interface BookService {
    BookDto addBook(BookDto bookDto,Integer authorId) throws AuthorNotExistOrNotFoundException;
    BookDto updateBook(Integer id, BookDto updatedBook) throws BookNotFoundException;
    void deleteBook(Integer id) throws BookNotFoundException;
    BookPageResponse searchBooksByTitle(String title,Integer pageNumber, 
    		Integer pageSize,String sortDir,String sortBy) throws BookNotFoundException;
    BookPageResponse searchBooksByAuthor(String author,Integer pageNumber,
    		Integer pageSize,String sortDir,String sortBy) throws BookNotFoundException;
    BookPageResponse searchBookByIsbn(String isbn,Integer pageNumber,
    		Integer pageSize,String sortDir,String sortBy) throws BookNotFoundException;
    List<BookDto> filterBooksByCategory(String category,Integer pageNumber, 
    		Integer pageSize,String sortDir,String sortBy) throws BookNotFoundException;
    void updateStock(Integer bookId, int quantity) throws BookNotFoundException;
    int getStock(Integer bookId) throws BookNotFoundException;
    BookDto getBookDetails(Integer id) throws BookNotFoundException;
    ReviewDto addReview(Integer bookId, ReviewDto review) throws BookNotFoundException;
    ReviewDto updateReview(Integer reviewId, ReviewDto updatedReview) throws ReviewNotFoundException;
    void deleteReview(Integer reviewId) throws ReviewNotFoundException;
    List<ReviewDto> getReviews(Integer bookId) throws ReviewNotFoundException, BookNotFoundException;
    void addToWishlist(Integer userId, Integer bookId) throws BookNotFoundException, UserNotFoundException;
    void removeFromWishlist(Integer userId, Integer bookId) throws UserNotFoundException, BookNotFoundException;
    List<BookDto> getWishlist(Integer userId,Integer pageNumber, Integer 
    		pageSize,String sortDir,String sortBy) throws UserNotFoundException;
    boolean isBookAvailable(Integer bookId);
}
