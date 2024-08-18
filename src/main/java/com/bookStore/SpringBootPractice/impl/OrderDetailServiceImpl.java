package com.bookStore.SpringBootPractice.impl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.appConstant.OrderDetailPageResponse;
import com.bookStore.SpringBootPractice.entities.Book;
import com.bookStore.SpringBootPractice.entities.Order;
import com.bookStore.SpringBootPractice.entities.OrderDetail;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.OrderDetailNotFoundOrNotExistException;
import com.bookStore.SpringBootPractice.exceptions.OrderNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.OrderDetailDto;
import com.bookStore.SpringBootPractice.payloads.OrderDto;
import com.bookStore.SpringBootPractice.repositories.BookRepository;
import com.bookStore.SpringBootPractice.repositories.OrderDetailRepository;
import com.bookStore.SpringBootPractice.repositories.OrderRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.service.OrderDetailService;
@Service
public class OrderDetailServiceImpl implements OrderDetailService{
	
    private OrderDetailRepository orderDetailRepository;
    private OrderRepository orderRepository;
    private BookRepository bookRepository;
    private Principal principal;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    
	public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository,
			BookRepository bookRepository,ModelMapper modelMapper,UserRepository userRepository) {
		super();
		this.orderDetailRepository = orderDetailRepository;
		this.orderRepository = orderRepository;
		this.bookRepository = bookRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
	}

	@Override
	public OrderDetailDto createOrderDetail(OrderDetailDto orderDetailDto, Integer bookId, Integer orderId) throws OrderNotFoundException, BookNotFoundException, UserNotFoundException {
	    if (orderDetailDto == null && bookId == null && orderId == null) {
	        throw new NullPointerException("OrderDetailDto, bookId, and orderId cannot be null.");
	    }
	    String username = principal.getName();
	    User user = this.userRepository.findByUsername(username).get();
	    Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found."));
	    Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));
	    OrderDetail orderDetail = this.modelMapper.map(orderDetailDto, OrderDetail.class);
	    if(!user.getId().equals(orderDetail.getOrder().getUser().getId())) {
	    	 throw new SecurityException("You are not authorized to create this orderDetail.");
	    }
	    orderDetail.setBook(book);
	    orderDetail.setOrder(order);
	    OrderDetail saveOrderDetail = this.orderDetailRepository.save(orderDetail);
	    return this.modelMapper.map(saveOrderDetail, OrderDetailDto.class);
	}


	@Override
	public OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto, Integer orderDetailId) throws OrderDetailNotFoundOrNotExistException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		OrderDetail orderDetail = this.orderDetailRepository.findById(orderDetailId).orElseThrow(()->
		new OrderDetailNotFoundOrNotExistException("OrderDetail with id " + orderDetailId + "not found or not exist"));
		if(orderDetailDto == null) {
			throw new IllegalArgumentException("OrderDetailDto can not be null !!");
		} 
		if(!user.getId().equals(orderDetail.getOrder().getUser().getId())) {
	    	 throw new SecurityException("You are not authorized to update this orderDetail.");
	    }
		orderDetail.setPrice(orderDetailDto.getPrice());
		orderDetail.setQuantity(orderDetailDto.getQuantity());
		OrderDetail updatedOrderDetail = this.orderDetailRepository.save(orderDetail);
		return this.modelMapper.map(updatedOrderDetail, OrderDetailDto.class);
	}

	@Override
	public void deleteOrderDetail(Integer orderDetailId) throws OrderDetailNotFoundOrNotExistException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		if(orderDetailId == null) {
			throw new NullPointerException("Order detail ID cannot be null");
		}
		
		OrderDetail orderDetail = this.orderDetailRepository.findById(orderDetailId).orElseThrow(()->
		new OrderDetailNotFoundOrNotExistException("OrderDetail with id " + orderDetailId + "not found or not exist"));
		if(!user.getId().equals(orderDetail.getOrder().getUser().getId())) {
	    	 throw new SecurityException("You are not authorized to delete this orderDetail.");
		}
		this.orderDetailRepository.delete(orderDetail);
		
		
	}

	@Override
	public OrderDetailDto getOrderDetailById(Integer orderDetailId) throws OrderDetailNotFoundOrNotExistException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		if(orderDetailId == null) {
			throw new NullPointerException("Order detail ID cannot be null");
		}
		OrderDetail orderDetail = this.orderDetailRepository.findById(orderDetailId).orElseThrow(()->
		new OrderDetailNotFoundOrNotExistException("OrderDetail with id " + orderDetailId + "not found or not exist"));
		if(!user.getId().equals(orderDetail.getOrder().getUser().getId())) {
	    	 throw new SecurityException("You are not authorized to fetch this orderDetail.");
		}
		return this.modelMapper.map(orderDetail, OrderDetailDto.class);
	}

	@Override
	public OrderDetailPageResponse getOrderDetailsByOrderId(Integer orderId, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) throws OrderDetailNotFoundOrNotExistException, OrderNotFoundException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		Order order = this.orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("Order not exist or not found with id " + orderId));
		if(!user.getId().equals(order.getUser().getId())) {
			throw new SecurityException("You are not authorized to fetch these orderDetail.");
		}
	    Sort sort;
	    if (sortDir.equals("asc")) {
	        sort = Sort.by(sortBy).ascending();
	    } else {
	        sort = Sort.by(sortBy).descending();
	    }
	    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
	    Page<OrderDetail> pages = this.orderDetailRepository.findByOrderId(orderId, p);
	    List<OrderDetail> orderDetail = pages.getContent();
	   
	    List<OrderDetailDto> detail = orderDetail.stream()
	            .map(orderDetailDto -> this.modelMapper.map(orderDetailDto, OrderDetailDto.class))
	            .collect(Collectors.toList());
	    OrderDetailPageResponse response = new OrderDetailPageResponse();
	    response.setContent(detail);
	    response.setPageNumber(pageNumber);
	    response.setPageSize(pages.getSize());
	    response.setTotalPages(pages.getTotalPages());
	    response.setTotalElement(pages.getTotalElements());
	    response.setLastpage(pages.isLast());
	    return response;
	}
	@Override
	public OrderDetailPageResponse getOrderDetailsByBookId(Integer bookId, Integer pageSize, Integer pageNumber, String sortBy, String sortDir) {
	    Sort sort;
	    if (sortDir.equals("asc")) {
	        sort = Sort.by(sortBy).ascending();
	    } else {
	        sort = Sort.by(sortBy).descending();
	    }
	    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
	    Page<OrderDetail> pages = this.orderDetailRepository.findByBookId(bookId, p);
	    List<OrderDetail> orderDetail = pages.getContent();
	    List<OrderDetailDto> detail = orderDetail.stream()
	            .map(orderDetailDto -> this.modelMapper.map(orderDetailDto, OrderDetailDto.class))
	            .collect(Collectors.toList());
	    OrderDetailPageResponse response = new OrderDetailPageResponse();
	    response.setContent(detail);
	    response.setPageNumber(pageNumber);
	    response.setPageSize(pageSize);
	    response.setTotalPages(pages.getTotalPages());
	    response.setTotalElement(pages.getTotalElements());
	    response.setLastpage(pages.isLast());
	    return response;
	}

	@Override
	public Double calculateTotalPriceForOrder(Integer orderId) {
	    if (orderId == null) {
	        throw new NullPointerException("Order ID is null");
	    }
	    List<OrderDetail> orderDetails = this.orderDetailRepository.findByOrderId(orderId);
	    return orderDetails.stream()
	            .mapToDouble(OrderDetail::getPrice)
	            .sum();
	}

	@Override
	public boolean checkBookAvailabilityInOrder(Integer orderId) {
	    if (orderId == null) {
	        throw new NullPointerException("Order ID is null");
	    }
	    List<OrderDetail> orderDetails = this.orderDetailRepository.findByOrderId(orderId);
	    return orderDetails.stream()
	            .anyMatch(orderDetail -> orderDetail.getBook().getStockQuantity() > 0);
	}

	@Override
	public OrderDetailPageResponse getOrderDetails(Integer pageSize, Integer pageNumber, String sortBy, String sortDir) {
	    Sort sort;
	    if (sortDir.equals("asc")) {
	        sort = Sort.by(sortBy).ascending();
	    } else {
	        sort = Sort.by(sortBy).descending();
	    }
	    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
	    Page<OrderDetail> pages = this.orderDetailRepository.findAll(p);
	    List<OrderDetail> orderDetails = pages.getContent();
	    List<OrderDetailDto> detail = orderDetails.stream()
	            .map(orderDetail -> this.modelMapper.map(orderDetail, OrderDetailDto.class))
	            .collect(Collectors.toList());
	    OrderDetailPageResponse response = new OrderDetailPageResponse();
	    response.setContent(detail);
	    response.setPageNumber(pageNumber);
	    response.setPageSize(pageSize);
	    response.setTotalPages(pages.getTotalPages());
	    return response;
	}
}
