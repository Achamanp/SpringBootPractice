package com.bookStore.SpringBootPractice.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.OrderDetail;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

	Page<OrderDetail> findByOrderId(Integer orderId, Pageable p);

	Page<OrderDetail> findByBookId(Integer bookId, Pageable p);

	List<OrderDetail> findByOrderId(Integer orderId);

	
}
