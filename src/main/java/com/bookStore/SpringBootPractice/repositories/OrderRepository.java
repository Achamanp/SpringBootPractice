package com.bookStore.SpringBootPractice.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookStore.SpringBootPractice.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	Page<Order> findByUserId(Integer userId, Pageable pageable);
	 Page<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

	Page<Order> findByPaymentMethod(String paymentMethod, Pageable pageable);
	Page<Order> findByStatus(String status, Pageable pageable);
	Page<Order> findByTotalAmountBetween(Double minAmount, Double maxAmount, Pageable pageable);
	boolean existsById(Integer longValue);
	
}
