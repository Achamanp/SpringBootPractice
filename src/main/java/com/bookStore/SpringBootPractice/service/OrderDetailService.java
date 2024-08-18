package com.bookStore.SpringBootPractice.service;

import com.bookStore.SpringBootPractice.payloads.OrderDetailDto;
import com.bookStore.SpringBootPractice.appConstant.OrderDetailPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.OrderDetailNotFoundOrNotExistException;
import com.bookStore.SpringBootPractice.exceptions.OrderNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;

import java.util.List;

public interface OrderDetailService {

    OrderDetailDto createOrderDetail(OrderDetailDto orderDetailDto,Integer bookId,Integer orderId ) throws OrderNotFoundException, BookNotFoundException, UserNotFoundException;
    OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto, Integer orderDetailId) throws OrderDetailNotFoundOrNotExistException;
    void deleteOrderDetail(Integer orderDetailId) throws OrderDetailNotFoundOrNotExistException;
    OrderDetailDto getOrderDetailById(Integer orderDetailId) throws OrderDetailNotFoundOrNotExistException;
    OrderDetailPageResponse getOrderDetailsByOrderId(Integer orderId,Integer pageSize, Integer pageNumber, String sortBy, String sortDir)throws  OrderDetailNotFoundOrNotExistException, OrderNotFoundException;
    OrderDetailPageResponse getOrderDetailsByBookId(Integer bookId,Integer pageSize, Integer pageNumber, String sortBy, String sortDir);
    Double calculateTotalPriceForOrder(Integer orderId);
    boolean checkBookAvailabilityInOrder(Integer orderId);
    OrderDetailPageResponse getOrderDetails(Integer pageSize, Integer pageNumber, String sortBy, String sortDir);
   // OrderDetailPageResponse searchOrderDetails(String keyword,Integer pageSize, Integer pageNumber, String sortBy, String sortDir);
}
