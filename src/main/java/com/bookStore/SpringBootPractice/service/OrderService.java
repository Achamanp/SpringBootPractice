package com.bookStore.SpringBootPractice.service;
import java.util.List;
import java.util.Optional;

import com.bookStore.SpringBootPractice.appConstant.OrderPageResponse;
import com.bookStore.SpringBootPractice.exceptions.OrderNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.OrderDto;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto) throws UserNotFoundException;
    OrderDto updateOrder(OrderDto orderDto) throws OrderNotFoundException, UserNotFoundException;
    void deleteOrder(Integer id) throws OrderNotFoundException, UserNotFoundException;
    OrderDto getOrderById(Integer id) throws OrderNotFoundException, UserNotFoundException;
    OrderPageResponse getAllOrders(Integer pageSize, Integer pageNumber,String sortDir,String sortBy);
    OrderPageResponse getOrdersByUserId(Integer userId,Integer pageSize, Integer pageNumber,String sortDir,String sortBy) throws UserNotFoundException;
    OrderPageResponse getOrdersByStatus(String status,Integer pageSize, Integer pageNumber,String sortDir,String sortBy) throws OrderNotFoundException;
    OrderPageResponse getOrdersByDateRange(String startDate, String endDate,Integer pageSize, Integer pageNumber,String sortDir,String sortBy) throws OrderNotFoundException;
    OrderPageResponse getOrdersByPaymentMethod(String paymentMethod,Integer pageSize, Integer pageNumber,String sortDir,String sortBy) throws OrderNotFoundException;
    OrderPageResponse getOrdersByTotalAmountRange(Double minAmount, Double maxAmount,Integer pageSize, Integer pageNumber,String sortDir,String sortBy) throws OrderNotFoundException;
    boolean orderExist(Integer id);
    OrderDto processOrder(Integer id, String status) throws OrderNotFoundException;
    OrderDto addCommentToOrder(Integer id, String comment) throws OrderNotFoundException;
    OrderDto updateShippingAddress(Integer id, String newAddress) throws OrderNotFoundException;
    OrderDto cancelOrder(Integer id) throws Exception;
    OrderDto reorder(Integer id) throws OrderNotFoundException;
    OrderPageResponse getOrderHistoryByCustomer(Integer customerId,Integer pageSize, Integer pageNumber,String sortDir,String sortBy) throws OrderNotFoundException;
    OrderPageResponse getRecentOrders(Integer limit,Integer pageSize, Integer pageNumber,String sortDir,String sortBy) throws OrderNotFoundException;

}