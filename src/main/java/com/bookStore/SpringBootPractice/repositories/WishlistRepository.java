package com.bookStore.SpringBootPractice.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.Book;
import com.bookStore.SpringBootPractice.entities.Wishlist;
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer>{
	 @Query("SELECT w.book FROM Wishlist w WHERE w.user.id = :userId")
	Page<Book> findBooksByUserId(Integer userId, Pageable pageable);

	Wishlist findByBookId(Integer bookId);

	Wishlist findByUserId(Integer id);
	 @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId")
	    List<Wishlist> findWishlistsByUserId(@Param("userId") Integer userId);


}