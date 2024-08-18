package com.bookStore.SpringBootPractice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.Book;
import com.bookStore.SpringBootPractice.entities.Review;
import com.bookStore.SpringBootPractice.entities.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

	org.springframework.data.domain.Page<Review> findByBookId(Integer bookId, Pageable p);

	Page<Review> findByUserId(Integer userId, Pageable pageable);
	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
	Double calculateAverageRating(Integer bookId);
	boolean existsByUserAndBook(User user, Book book);
	@Query("SELECT r FROM Review r WHERE LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Review> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
	@Query("SELECT r FROM Review r WHERE r.book = :book ORDER BY r.rating DESC")
    Page<Review> findTopNByBookOrderByRatingDesc(@Param("book") Book book, Pageable pageable);
	Long countByBook(Book book);

	

}
