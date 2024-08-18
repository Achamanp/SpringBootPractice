package com.bookStore.SpringBootPractice.controller;

import com.bookStore.SpringBootPractice.appConstant.ReviewPageResponse;
import com.bookStore.SpringBootPractice.payloads.ReviewDto;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ResourceNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ReviewNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.impl.ReviewServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewServiceImpl) {
        this.reviewServiceImpl = reviewServiceImpl;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto, @PathVariable Integer bookId, Principal principal) {
        try {
            ReviewDto createdReview = reviewServiceImpl.createReview(reviewDto, bookId);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (BookNotFoundException | UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@RequestBody ReviewDto reviewDto, @PathVariable Integer reviewId, Principal principal) {
        try {
            ReviewDto updatedReview = reviewServiceImpl.updateReview(reviewDto, reviewId);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId, Principal principal) {
        try {
            reviewServiceImpl.deleteReview(reviewId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Integer reviewId, Principal principal) {
        try {
            ReviewDto reviewDto = reviewServiceImpl.getReviewById(reviewId);
            return new ResponseEntity<>(reviewDto, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ReviewPageResponse> getReviewsByBookId(@PathVariable Integer bookId,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam Integer pageNumber,
                                                                 @RequestParam String sortBy,
                                                                 @RequestParam String sortDir) {
        try {
            ReviewPageResponse reviews = reviewServiceImpl.getReviewsByBookId(bookId, pageSize, pageNumber, sortBy, sortDir);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ReviewPageResponse> getReviewsByUserId(@PathVariable Integer userId,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam Integer pageNumber,
                                                                 @RequestParam String sortBy,
                                                                 @RequestParam String sortDir) {
        try {
            ReviewPageResponse reviews = reviewServiceImpl.getReviewsByUserId(userId, pageSize, pageNumber, sortBy, sortDir);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/average-rating/{bookId}")
    public ResponseEntity<Double> calculateAverageRatingForBook(@PathVariable Integer bookId) throws BookNotFoundException {
        Double averageRating = reviewServiceImpl.calculateAverageRatingForBook(bookId);
		return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }

    @GetMapping("/has-reviewed/{userId}/{bookId}")
    public ResponseEntity<Boolean> hasUserReviewedBook(@PathVariable Integer userId, @PathVariable Integer bookId) {
        try {
            boolean hasReviewed = reviewServiceImpl.hasUserReviewedBook(userId, bookId);
            return new ResponseEntity<>(hasReviewed, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/top-reviews/{bookId}")
    public ResponseEntity<ReviewPageResponse> getTopReviewsForBook(@PathVariable Integer bookId,
                                                                   @RequestParam int topN,
                                                                   @RequestParam Integer pageSize,
                                                                   @RequestParam Integer pageNumber,
                                                                   @RequestParam String sortBy,
                                                                   @RequestParam String sortDir) {
        try {
            ReviewPageResponse reviews = reviewServiceImpl.getTopReviewsForBook(bookId, topN, pageSize, pageNumber, sortBy, sortDir);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/count/{bookId}")
    public ResponseEntity<Long> countReviewsForBook(@PathVariable Integer bookId) {
        try {
            Long count = reviewServiceImpl.countReviewsForBook(bookId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}