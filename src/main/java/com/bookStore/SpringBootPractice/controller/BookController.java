package com.bookStore.SpringBootPractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.bookStore.SpringBootPractice.appConstant.BookPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ReviewNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.BookDto;
import com.bookStore.SpringBootPractice.payloads.ReviewDto;
import com.bookStore.SpringBootPractice.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto,@RequestParam("authorId") Integer authorId) {
        try {
            BookDto createdBook = bookService.addBook(bookDto,authorId);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding book", e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> updateBook(@PathVariable Integer id, @RequestBody BookDto updatedBook) {
        try {
            BookDto book = bookService.updateBook(id, updatedBook);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating book", e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting book", e);
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<BookPageResponse> searchBooksByTitle(
            @RequestParam String title,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            BookPageResponse response = bookService.searchBooksByTitle(title, pageNumber, pageSize, sortDir, sortBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching books by title", e);
        }
    }

    @GetMapping("/search/author")
    public ResponseEntity<BookPageResponse> searchBooksByAuthor(
            @RequestParam String authorName,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            BookPageResponse response = bookService.searchBooksByAuthor(authorName, pageNumber, pageSize, sortDir, sortBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching books by author", e);
        }
    }

    @GetMapping("/search/isbn")
    public ResponseEntity<BookPageResponse> searchBookByIsbn(
            @RequestParam String isbn,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            BookPageResponse response = bookService.searchBookByIsbn(isbn, pageNumber, pageSize, sortDir, sortBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching books by ISBN", e);
        }
    }

    @GetMapping("/filter/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDto>> filterBooksByCategory(
            @RequestParam String category,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            List<BookDto> books = bookService.filterBooksByCategory(category, pageNumber, pageSize, sortDir, sortBy);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error filtering books by category", e);
        }
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> getStock(@PathVariable Integer id) {
        try {
            int stock = bookService.getStock(id);
            return new ResponseEntity<>(stock, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error retrieving stock", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookDetails(@PathVariable Integer id) {
        try {
            BookDto book = bookService.getBookDetails(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error retrieving book details", e);
        }
    }

    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Integer bookId, @RequestBody ReviewDto reviewDto) {
        try {
            ReviewDto review = bookService.addReview(bookId, reviewDto);
            return new ResponseEntity<>(review, HttpStatus.CREATED);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding review", e);
        }
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Integer reviewId, @RequestBody ReviewDto updatedReview) {
        try {
            ReviewDto review = bookService.updateReview(reviewId, updatedReview);
            return new ResponseEntity<>(review, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating review", e);
        }
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        try {
            bookService.deleteReview(reviewId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting review", e);
        }
    }

    @GetMapping("/{bookId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Integer bookId) {
        try {
            List<ReviewDto> reviews = bookService.getReviews(bookId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (ReviewNotFoundException | BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error retrieving reviews", e);
        }
    }

    @PostMapping("/{userId}/wishlist/{bookId}")
    public ResponseEntity<Void> addToWishlist(@PathVariable Integer userId, @PathVariable Integer bookId) {
        try {
            bookService.addToWishlist(userId, bookId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookNotFoundException | UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding to wishlist", e);
        }
    }

    @DeleteMapping("/{userId}/wishlist/{bookId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Integer userId, @PathVariable Integer bookId) {
        try {
            bookService.removeFromWishlist(userId, bookId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookNotFoundException | UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error removing from wishlist", e);
        }
    }

    @GetMapping("/{userId}/wishlist")
    public ResponseEntity<List<BookDto>> getWishlist(
            @PathVariable Integer userId,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            List<BookDto> wishlist = bookService.getWishlist(userId, pageNumber, pageSize, sortDir, sortBy);
            return new ResponseEntity<>(wishlist, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

