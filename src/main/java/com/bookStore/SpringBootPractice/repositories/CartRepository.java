package com.bookStore.SpringBootPractice.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{

	Cart findByUserId(Integer userId);
	 List<Cart> findAllByUserId(Integer userId);
	Page<Cart> findByUserId(Integer userId, Pageable p);
	
}
