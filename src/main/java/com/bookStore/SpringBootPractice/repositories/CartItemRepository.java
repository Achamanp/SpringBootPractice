package com.bookStore.SpringBootPractice.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	List<CartItem> findByCartId(Integer cartId);

	Page<CartItem> findByUserId(Integer userId, Pageable pageable);

	Page<CartItem> findByCartId(Integer cartId, Pageable pageable);

	

}
