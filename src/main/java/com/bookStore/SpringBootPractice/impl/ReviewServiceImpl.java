package com.bookStore.SpringBootPractice.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.appConstant.ReviewPageResponse;
import com.bookStore.SpringBootPractice.entities.Book;
import com.bookStore.SpringBootPractice.entities.Review;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ResourceNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ReviewNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.ReviewDto;
import com.bookStore.SpringBootPractice.repositories.BookRepository;
import com.bookStore.SpringBootPractice.repositories.ReviewRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.service.ReviewService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private Principal principal;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, BookRepository bookRepository,
                             UserRepository userRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto, Integer bookId) throws BookNotFoundException, UserNotFoundException {
    	String name = principal.getName();
    	User user = this.userRepository.findByUsername(name).orElseThrow(()-> new UserNotFoundException("User not found or not exist with username" + name));
    	
    	if(reviewDto==null && bookId ==null) {
    		 throw new NullPointerException("reviewDto, bookId, and orderId cannot be null.");
    	}
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found or not exist with id " + bookId));
        Review review = modelMapper.map(reviewDto, Review.class);
        review.setBook(book);
        review.setUser(user);
        review.setRating(reviewDto.getRating());

        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewDto.class);
    }

    @Override
    public ReviewDto updateReview(ReviewDto reviewDto, Integer reviewId) throws ReviewNotFoundException {
    	String username = principal.getName();
    	User user = this.userRepository.findByUsername(username).get();
    	if(reviewDto==null && reviewId ==null) {
   		 throw new NullPointerException("reviewDto and reviewId cannot be null.");
   	}
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not exist or not created yet with id "+ reviewId));
        if(user.getId().equals(review.getUser().getId())){
        	throw new SecurityException("You are not authorized to update this review.");
        }
       review.setContent(reviewDto.getContent());
       review.setRating(reviewDto.getRating());
        Review updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewDto.class);
    }

    @Override
    public void deleteReview(Integer reviewId) throws ReviewNotFoundException {
    	String username = principal.getName();
    	if(reviewId == null) {
    		throw new NullPointerException("Review can not be null");
    	}
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not exist or not created yet with id "+ reviewId));
        User user = this.userRepository.findByUsername(username).get();
        if(!review.getUser().getId().equals(user.getId())) {
        	throw new SecurityException("You are not authorized to delete this review.");
        }
        reviewRepository.delete(review);
    }

    @Override
    public ReviewDto getReviewById(Integer reviewId) throws ReviewNotFoundException {
    	String username = principal.getName();
    	if(reviewId == null) {
    		throw new NullPointerException("Review Id can not be null");
    	}
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->  new ReviewNotFoundException("Review not exist or not created yet with id "+ reviewId));
        User user = this.userRepository.findByUsername(username).get();
        if(!review.getUser().getId().equals(user.getId())) {
        	throw new SecurityException("You are not authorized to get this review.");
        }
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public ReviewPageResponse getReviewsByBookId(Integer bookId, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) {
        if(bookId==null) {
        	throw new NullPointerException("Book Id can not be null");
        }
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")) {
        	sort = Sort.by(sortBy).ascending();
        }else {
        	sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Review> pages = this.reviewRepository.findByBookId(bookId,p);
        List<Review> review = pages.getContent();
        List<ReviewDto> reviewDto = review.stream().map(dto-> 
        this.modelMapper.map(dto,ReviewDto.class)).collect(Collectors.toList());
        ReviewPageResponse response = new ReviewPageResponse();
        response.setContent(reviewDto);
        response.setLastpage(pages.isLast());
        response.setPageNumber(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalElement(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        return response;
    }

    @Override
    public ReviewPageResponse getReviewsByUserId(Integer userId, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) throws ReviewNotFoundException {
        if (userId == null) {
            throw new NullPointerException("User Id cannot be null");
        }

        Sort sort = null;
        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Review> pages = this.reviewRepository.findByUserId(userId, pageable);

        if (pages.isEmpty()) {
            throw new ReviewNotFoundException("No reviews found for the given user.");
        }

        List<Review> reviews = pages.getContent();
        List<ReviewDto> reviewDtos = reviews.stream()
            .map(review -> this.modelMapper.map(review, ReviewDto.class))
            .collect(Collectors.toList());

        ReviewPageResponse response = new ReviewPageResponse();
        response.setContent(reviewDtos);
        response.setLastpage(pages.isLast());
        response.setPageNumber(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalElement(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());

        return response;
    }

    @Override
    public Double calculateAverageRatingForBook(Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        Double averageRating = reviewRepository.calculateAverageRating(bookId);
        return averageRating != null ? averageRating : 0.0;
    }


    @Override
    public boolean hasUserReviewedBook(Integer userId, Integer bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        return reviewRepository.existsByUserAndBook(user, book);
    }

    @Override
    public ReviewPageResponse searchReviewsByKeyword(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) throws ReviewNotFoundException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        Sort sort = null;
        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Review> pages = this.reviewRepository.searchByKeyword(keyword, pageable);

        if (pages.isEmpty()) {
            throw new ReviewNotFoundException("No reviews found for the given keyword.");
        }

        List<Review> reviews = pages.getContent();
        List<ReviewDto> reviewDtos = reviews.stream()
            .map(review -> this.modelMapper.map(review, ReviewDto.class))
            .collect(Collectors.toList());

        ReviewPageResponse response = new ReviewPageResponse();
        response.setContent(reviewDtos);
        response.setLastpage(pages.isLast());
        response.setPageNumber(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalElement(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        return response;
    }



    @Override
    public ReviewPageResponse getTopReviewsForBook(Integer bookId, int topN, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book does not exist or may be currently out of stock"));

        Sort sort = Sort.by(sortBy);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber, topN, sort);
        Page<Review> page = reviewRepository.findTopNByBookOrderByRatingDesc(book, pageable);
        List<ReviewDto> reviewDtos = page.getContent().stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
        ReviewPageResponse response = new ReviewPageResponse();
        response.setContent(reviewDtos);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElement(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastpage(page.isLast());

        return response;
    }


    @Override
    public Long countReviewsForBook(Integer bookId) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
        return reviewRepository.countByBook(book);
    }
}

