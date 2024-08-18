package com.bookStore.SpringBootPractice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookStore.SpringBootPractice.exceptions.AuthorNotExistOrNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.impl.AuthorServiceImpl;
import com.bookStore.SpringBootPractice.payloads.AuthorDto;
import com.bookStore.SpringBootPractice.appConstant.AuthorPageResponse;
import com.bookStore.SpringBootPractice.appConstant.BookPageResponse;
import com.bookStore.SpringBootPractice.payloads.ApiResponse;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorServiceImpl authorServiceImpl;

    public AuthorController(AuthorServiceImpl authorServiceImpl) {
        this.authorServiceImpl = authorServiceImpl;
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto createdAuthor = authorServiceImpl.createAuthor(authorDto);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorDto> updateAuthor(@RequestBody AuthorDto authorDto, @PathVariable Integer authorId) {
        try {
            AuthorDto updatedAuthor = authorServiceImpl.updateAuthor(authorDto, authorId);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } catch (AuthorNotExistOrNotFoundException e) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<ApiResponse> deleteAuthor(@PathVariable Integer authorId) {
        try {
            authorServiceImpl.deleteAuthor(authorId);
            return new ResponseEntity<>(new ApiResponse(true, "Author deleted successfully"), HttpStatus.OK);
        } catch (AuthorNotExistOrNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Integer authorId) {
        try {
            AuthorDto author = authorServiceImpl.getAuthor(authorId);
            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (AuthorNotExistOrNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<AuthorPageResponse> getAllAuthors(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {
        AuthorPageResponse authorPageResponse = authorServiceImpl.getAllAuthor(pageSize, pageNumber, sortDir, sortBy);
        return new ResponseEntity<>(authorPageResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<AuthorPageResponse> searchAuthors(@RequestParam String keyword) {
        AuthorPageResponse authorPageResponse = authorServiceImpl.searchAuthors(keyword);
        return new ResponseEntity<>(authorPageResponse, HttpStatus.OK);
    }

    @PostMapping("/{authorId}/books/{bookId}")
    public ResponseEntity<ApiResponse> addBookToAuthor(@PathVariable Integer authorId, @PathVariable Integer bookId) {
        try {
            authorServiceImpl.addBookToAuthor(authorId, bookId);
            return new ResponseEntity<>(new ApiResponse(true, "Book added to author successfully"), HttpStatus.OK);
        } catch (AuthorNotExistOrNotFoundException | BookNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{authorId}/books/{bookId}")
    public ResponseEntity<ApiResponse> removeBookFromAuthor(@PathVariable Integer authorId, @PathVariable Integer bookId) {
        try {
            authorServiceImpl.removeBookFromAuthor(authorId, bookId);
            return new ResponseEntity<>(new ApiResponse(true, "Book removed from author successfully"), HttpStatus.OK);
        } catch (AuthorNotExistOrNotFoundException | BookNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{authorId}/books")
    public ResponseEntity<BookPageResponse> getBooksByAuthor(
            @PathVariable Integer authorId,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy) {
        try {
            BookPageResponse bookPageResponse = authorServiceImpl.getBooksByAuthor(authorId, pageSize, pageNumber, sortDir, sortBy);
            return new ResponseEntity<>(bookPageResponse, HttpStatus.OK);
        } catch (AuthorNotExistOrNotFoundException e) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }
}
