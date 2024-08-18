package com.bookStore.SpringBootPractice.service;

import com.bookStore.SpringBootPractice.appConstant.ReviewPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ReviewNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.ReviewDto;

public interface ReviewService{
	 ReviewDto createReview(ReviewDto reviewDto, Integer bookId) throws BookNotFoundException, UserNotFoundException;
	    ReviewDto updateReview(ReviewDto reviewDto, Integer reviewId) throws ReviewNotFoundException;
	    void deleteReview(Integer reviewId) throws ReviewNotFoundException;
	    ReviewDto getReviewById(Integer reviewId) throws ReviewNotFoundException;
	    ReviewPageResponse getReviewsByBookId(Integer bookId, Integer pageSize, Integer pageNumber, String sortBy, String sortDir);
	    ReviewPageResponse getReviewsByUserId(Integer userId, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) throws ReviewNotFoundException;
	    Double calculateAverageRatingForBook(Integer bookId);
	    boolean hasUserReviewedBook(Integer userId, Integer bookId);
	    ReviewPageResponse searchReviewsByKeyword(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) throws ReviewNotFoundException;
	    ReviewPageResponse getTopReviewsForBook(Integer bookId, int topN,Integer pageSize, Integer pageNumber, String sortBy, String sortDir) throws BookNotFoundException;
	    Long countReviewsForBook(Integer bookId) throws BookNotFoundException;
}
