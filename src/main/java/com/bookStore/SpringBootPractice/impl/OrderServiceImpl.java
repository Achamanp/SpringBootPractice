package com.bookStore.SpringBootPractice.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.appConstant.OrderPageResponse;
import com.bookStore.SpringBootPractice.entities.Order;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.exceptions.OrderNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.OrderDto;
import com.bookStore.SpringBootPractice.repositories.OrderRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.service.OrderService;
@Service
public class OrderServiceImpl implements OrderService{
	private OrderRepository orderRepository;
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private Principal principal;
	public OrderServiceImpl(OrderRepository orderRepository,ModelMapper modelMapper,UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
	}

	@Override
	public OrderDto createOrder(OrderDto orderDto) throws UserNotFoundException {
	    if (orderDto == null) {
	        throw new NullPointerException("OrderDto cannot be null. Please provide a valid OrderDto object to create an order.");
	    }
	   String name = principal.getName();
	    User user = this.userRepository.findByUsername(name).orElseThrow(()-> new UserNotFoundException("User not found with id " + name));
	    Order order = this.modelMapper.map(orderDto, Order.class);
	    order.setUser(user);
	    Order saveOrder = this.orderRepository.save(order);
		return this.modelMapper.map(saveOrder, OrderDto.class);
	}

	@Override
	public OrderDto updateOrder(OrderDto orderDto) throws OrderNotFoundException, UserNotFoundException {
	    String username = principal.getName();
	    User user = this.userRepository.findByUsername(username)
	                  .orElseThrow(() -> new UserNotFoundException("User not found with username " + username));

	    if (orderDto == null) {
	        throw new NullPointerException("OrderDto cannot be null. Please provide a valid OrderDto object to update an order.");
	    }
	    Order order = this.orderRepository.findById(orderDto.getId())
	                 .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + orderDto.getId()));
	    if (!order.getUser().getId().equals(user.getId())) {
	        throw new SecurityException("You are not authorized to update this order.");
	    }
	    order.setComment(orderDto.getComment());
	    order.setPaymentMethod(orderDto.getPaymentMethod());
	    order.setShippingAddress(orderDto.getShippingAddress());
	    order.setStatus(orderDto.getStatus());
	    order.setTotalAmount(orderDto.getTotalAmount());
	    order.setUpdateDate(LocalDate.now());
	    Order updatedOrder = this.orderRepository.save(order);
	    return this.modelMapper.map(updatedOrder, OrderDto.class);
	}


	@Override
	public void deleteOrder(Integer id) throws OrderNotFoundException, UserNotFoundException {
	    String username = principal.getName();
	    User user = this.userRepository.findByUsername(username)
	                  .orElseThrow(() -> new UserNotFoundException("User not found with name " + username));
	    Order order = this.orderRepository.findById(id)
	                 .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));
	    if (!order.getUser().getId().equals(user.getId())) {
	        throw new SecurityException("You are not authorized to delete this order.");
	    }
	    this.orderRepository.delete(order);
	}

	@Override
	public OrderDto getOrderById(Integer id) throws OrderNotFoundException, UserNotFoundException {
		String username = principal.getName();
		Order order = this.orderRepository.findById(id).get();
		if(order==null) {
			throw new OrderNotFoundException("Order not found with id " + id);
		}
		 User user = this.userRepository.findByUsername(username)
                 .orElseThrow(() -> new UserNotFoundException("User not found with name " + username));
		 if(!order.getUser().getId().equals(user.getId())) {
			 throw new SecurityException("You are not authorized to delete this order.");
		 }
		return this.modelMapper.map(order, OrderDto.class);
	}

	public OrderPageResponse getAllOrders(Integer pageSize, Integer pageNumber, String sortDir, String sortBy) {
	    Sort sort;
	    if (sortDir.equalsIgnoreCase("asc")) {
	        sort = Sort.by(sortBy).ascending();
	    } else if (sortDir.equalsIgnoreCase("desc")) {
	        sort = Sort.by(sortBy).descending();
	    } else {
	        sort = Sort.by(sortBy).ascending();
	    }
	    Pageable pageable;
	    if (pageNumber != null && pageSize != null) {
	        pageable = PageRequest.of(pageNumber, pageSize, sort);
	    } else if (pageNumber != null) {
	        pageable = PageRequest.of(pageNumber, 10, sort);
	    } else if (pageSize != null) {
	        pageable = PageRequest.of(0, pageSize, sort); 
	    } else {
	        pageable = PageRequest.of(0, 10, sort); 
	    }
	    Page<Order> ordersPage = orderRepository.findAll(pageable);
	    List<Order> orders = ordersPage.getContent();

	    List<OrderDto> orderDtos = new ArrayList<>();
	    for (Order order : orders) {
	        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
	        orderDtos.add(orderDto);
	    }
	    OrderPageResponse response = new OrderPageResponse();
	   response.setContent(orderDtos);
	   response.setLastpage(ordersPage.isLast());
	   response.setPageNumber(ordersPage.getNumber());
	   response.setPageSize(ordersPage.getSize());
	   response.setTotalElement(ordersPage.getTotalElements());
	   response.setTotalPages(ordersPage.getTotalPages());
	    return response;
	}

	@Override
	public OrderPageResponse getOrdersByUserId(Integer userId, Integer pageSize, Integer pageNumber, String sortDir, String sortBy) throws UserNotFoundException {
		if(userId == null) {
			throw new NullPointerException("User id can not be null");
		}
	    int page = pageNumber != null ? pageNumber : 0;
	    int size = pageSize != null ? pageSize : 10;

	    Pageable pageable;
	    if (sortDir != null && sortBy != null) {
	        if (sortDir.equalsIgnoreCase("asc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
	        } else if (sortDir.equalsIgnoreCase("desc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
	        } else {
	            pageable = PageRequest.of(page, size);
	        }
	    } else {
	        pageable = PageRequest.of(page, size);
	    }

	    Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
	    if(orderPage==null) {
	    	throw new UsernameNotFoundException("User not found with  id " + userId);
	    }
	    List<Order> orders = orderPage.getContent();

	    List<OrderDto> orderDTOs = orders.stream().map(order->
	    this.modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());
	    OrderPageResponse response = new OrderPageResponse();
	    response.setContent(orderDTOs);
	    response.setLastpage(orderPage.isLast());
	    response.setPageNumber(orderPage.getNumber());
	    response.setPageSize(orderPage.getSize());
	    response.setTotalElement(orderPage.getTotalElements());
	    response.setTotalPages(orderPage.getTotalPages());
	    return response;
	}

	@Override
	public OrderPageResponse getOrdersByStatus(String status, Integer pageSize, Integer pageNumber,
			String sortDir, String sortBy) throws OrderNotFoundException {
	    int page = pageNumber != null ? pageNumber : 0;
	    int size = pageSize != null ? pageSize : 10;

	    Pageable pageable;
	    if (sortDir != null && sortBy != null) {
	        if (sortDir.equalsIgnoreCase("asc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
	        } else if (sortDir.equalsIgnoreCase("desc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
	        } else {
	            pageable = PageRequest.of(page, size);
	        }
	    } else {
	        pageable = PageRequest.of(page, size);
	    }

	    Page<Order> orderPage = orderRepository.findByStatus(status, pageable);
	    if (orderPage == null) {
	        throw new OrderNotFoundException("No orders found with status " + status);
	    }

	    List<Order> orders = orderPage.getContent();

	    List<OrderDto> orderDTOs = orders.stream()
	            .map(order -> modelMapper.map(order, OrderDto.class))
	            .collect(Collectors.toList());

	    OrderPageResponse response = new OrderPageResponse();
	    response.setContent(orderDTOs);
	    response.setLastpage(orderPage.isLast());
	    response.setPageNumber(orderPage.getNumber());
	    response.setPageSize(orderPage.getSize());
	    response.setTotalElement(orderPage.getTotalElements());
	    response.setTotalPages(orderPage.getTotalPages());

	    return response;
	}

	@Override
	public OrderPageResponse getOrdersByDateRange(String startDate, String endDate, Integer pageSize, Integer pageNumber, 
	                                              String sortDir, String sortBy) throws OrderNotFoundException {
	    int page = pageNumber != null ? pageNumber : 0;
	    int size = pageSize != null ? pageSize : 10;

	    Pageable pageable;
	    if (sortDir != null && sortBy != null) {
	        if (sortDir.equalsIgnoreCase("asc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
	        } else if (sortDir.equalsIgnoreCase("desc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
	        } else {
	            pageable = PageRequest.of(page, size);
	        }
	    } else {
	        pageable = PageRequest.of(page, size);
	    }
	    LocalDate start = LocalDate.parse(startDate);
	    LocalDate end = LocalDate.parse(endDate);
	    Page<Order> orderPage = orderRepository.findByOrderDateBetween(start, end, pageable);
	    if (orderPage == null) {
	        throw new OrderNotFoundException("No orders found between " + startDate + " and " + endDate);
	    }

	    List<Order> orders = orderPage.getContent();

	    List<OrderDto> orderDTOs = orders.stream()
	            .map(order -> modelMapper.map(order, OrderDto.class))
	            .collect(Collectors.toList());

	    OrderPageResponse response = new OrderPageResponse();
	    response.setContent(orderDTOs);
	    response.setLastpage(orderPage.isLast());
	    response.setPageNumber(orderPage.getNumber());
	    response.setPageSize(orderPage.getSize());
	    response.setTotalElement(orderPage.getTotalElements());
	    response.setTotalPages(orderPage.getTotalPages());

	    return response;
	}

	@Override
	public OrderPageResponse getOrdersByPaymentMethod(String paymentMethod, Integer pageSize, Integer pageNumber, 
	                                                   String sortDir, String sortBy) throws OrderNotFoundException {
	    int page = pageNumber != null ? pageNumber : 0;
	    int size = pageSize != null ? pageSize : 10;

	    Pageable pageable;
	    if (sortDir != null && sortBy != null) {
	        if (sortDir.equalsIgnoreCase("asc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
	        } else if (sortDir.equalsIgnoreCase("desc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
	        } else {
	            pageable = PageRequest.of(page, size);
	        }
	    } else {
	        pageable = PageRequest.of(page, size);
	    }

	    Page<Order> orderPage = orderRepository.findByPaymentMethod(paymentMethod, pageable);
	    if (orderPage == null || orderPage.getContent().isEmpty()) {
	        throw new OrderNotFoundException("No orders found with payment method " + paymentMethod);
	    }

	    List<Order> orders = orderPage.getContent();

	    List<OrderDto> orderDTOs = orders.stream()
	            .map(order -> modelMapper.map(order, OrderDto.class))
	            .collect(Collectors.toList());

	    OrderPageResponse response = new OrderPageResponse();
	    response.setContent(orderDTOs);
	    response.setLastpage(orderPage.isLast());
	    response.setPageNumber(orderPage.getNumber());
	    response.setPageSize(orderPage.getSize());
	    response.setTotalElement(orderPage.getTotalElements());
	    response.setTotalPages(orderPage.getTotalPages());

	    return response;
	}

	@Override
	public OrderPageResponse getOrdersByTotalAmountRange(Double minAmount, Double maxAmount, Integer pageSize, 
	                                                      Integer pageNumber, String sortDir, String sortBy) throws OrderNotFoundException {
	    int page = pageNumber != null ? pageNumber : 0;
	    int size = pageSize != null ? pageSize : 10;

	    Pageable pageable;
	    if (sortDir != null && sortBy != null) {
	        if (sortDir.equalsIgnoreCase("asc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
	        } else if (sortDir.equalsIgnoreCase("desc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
	        } else {
	            pageable = PageRequest.of(page, size);
	        }
	    } else {
	        pageable = PageRequest.of(page, size);
	    }

	    Page<Order> orderPage = orderRepository.findByTotalAmountBetween(minAmount, maxAmount, pageable);
	    if (orderPage == null || orderPage.getContent().isEmpty()) {
	        throw new OrderNotFoundException("No orders found within the amount range " + minAmount + " to " + maxAmount);
	    }

	    List<Order> orders = orderPage.getContent();

	    List<OrderDto> orderDTOs = orders.stream()
	            .map(order -> modelMapper.map(order, OrderDto.class))
	            .collect(Collectors.toList());

	    OrderPageResponse response = new OrderPageResponse();
	    response.setContent(orderDTOs);
	    response.setLastpage(orderPage.isLast());
	    response.setPageNumber(orderPage.getNumber());
	    response.setPageSize(orderPage.getSize());
	    response.setTotalElement(orderPage.getTotalElements());
	    response.setTotalPages(orderPage.getTotalPages());
	    return response;
	}

	@Override
	public boolean orderExist(Integer id) {
	    if (id == null) {
	        throw new IllegalArgumentException("Order ID must not be null.");
	    }

	    try {
	        return orderRepository.existsById(id);
	    } catch (Exception e) {
	        throw new RuntimeException("An unexpected error occurred while checking if the order exists with ID " + id, e);
	    }
	}

	@Override
	public OrderDto processOrder(Integer id, String status) throws OrderNotFoundException {
	    if (id == null) {
	        throw new IllegalArgumentException("Order ID must not be null.");
	    }

	    if (status == null || status.trim().isEmpty()) {
	        throw new IllegalArgumentException("Order status must not be null or empty.");
	    }

	    try {
	        Order order = orderRepository.findById(id)
	                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found."));

	        order.setStatus(status);
	        Order updatedOrder = orderRepository.save(order);

	        return modelMapper.map(updatedOrder, OrderDto.class);
	    } catch (OrderNotFoundException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new RuntimeException("An unexpected error occurred while processing the order with ID " + id, e);
	    }
	}

	@Override
	public OrderDto addCommentToOrder(Integer id, String comment) throws OrderNotFoundException {
	    if (id == null) {
	        throw new IllegalArgumentException("Order ID must not be null.");
	    }

	    if (comment == null || comment.trim().isEmpty()) {
	        throw new IllegalArgumentException("Comment must not be null or empty.");
	    }

	    try {
	        Order order = orderRepository.findById(id)
	                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found."));

	        order.setComment(comment);
	        Order updatedOrder = orderRepository.save(order);

	        return modelMapper.map(updatedOrder, OrderDto.class);
	    } catch (OrderNotFoundException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new RuntimeException("An unexpected error occurred while adding a comment to the order with ID " + id, e);
	    }
	}

	@Override
	public OrderDto updateShippingAddress(Integer id, String newAddress) throws OrderNotFoundException {
	    if (id == null) {
	        throw new IllegalArgumentException("Order ID must not be null.");
	    }

	    if (newAddress == null || newAddress.trim().isEmpty()) {
	        throw new IllegalArgumentException("New shipping address must not be null or empty.");
	    }

	    try {
	        Order order = orderRepository.findById(id)
	                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found."));

	        order.setShippingAddress(newAddress.trim());
	        Order updatedOrder = orderRepository.save(order);

	        return modelMapper.map(updatedOrder, OrderDto.class);
	    } catch (OrderNotFoundException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new RuntimeException("An unexpected error occurred while updating the shipping address for the order with ID " + id, e);
	    }
	}

	@Override
	public OrderDto cancelOrder(Integer id) throws Exception {
	    if (id == null) {
	        throw new IllegalArgumentException("Order ID must not be null.");
	    }

	    try {
	        Order order = orderRepository.findById(id)
	                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found."));

	        if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
	            throw new IllegalStateException("Order with ID " + id + " is already cancelled.");
	        }

	        if (!"Processing".equalsIgnoreCase(order.getStatus())) {
	            throw new IllegalStateException("Order with ID " + id + " cannot be cancelled as it is already in " + order.getStatus() + " status.");
	        }

	        order.setStatus("Cancelled");
	        Order updatedOrder = orderRepository.save(order);

	        return modelMapper.map(updatedOrder, OrderDto.class);
	    } catch (OrderNotFoundException | IllegalStateException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new RuntimeException("An unexpected error occurred while cancelling the order with ID " + id, e);
	    }
	}


	@Override
	public OrderDto reorder(Integer id) throws OrderNotFoundException {
	    if (id == null) {
	        throw new IllegalArgumentException("Order ID must not be null.");
	    }

	    try {
	        Order originalOrder = orderRepository.findById(id)
	                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found."));

	        Order newOrder = new Order();
	        newOrder.setUser(originalOrder.getUser());
	        newOrder.setShippingAddress(originalOrder.getShippingAddress());
	        newOrder.setTotalAmount(originalOrder.getTotalAmount());
	        newOrder.setStatus("Processing");
	        newOrder.setOrderDate(LocalDate.now());

	        Order savedOrder = orderRepository.save(newOrder);

	        return modelMapper.map(savedOrder, OrderDto.class);
	    } catch (OrderNotFoundException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new RuntimeException("An unexpected error occurred while reordering based on order with ID " + id, e);
	    }
	}

	@Override
	public OrderPageResponse getOrderHistoryByCustomer(Integer userId, Integer pageSize, Integer pageNumber,
	                                                   String sortDir, String sortBy) throws OrderNotFoundException {
	    if (userId == null) {
	        throw new IllegalArgumentException("Customer ID must not be null.");
	    }

	    int page = pageNumber != null ? pageNumber : 0;
	    int size = pageSize != null ? pageSize : 10;

	    Pageable pageable;
	    if (sortDir != null && sortBy != null) {
	        if (sortDir.equalsIgnoreCase("asc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
	        } else if (sortDir.equalsIgnoreCase("desc")) {
	            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
	        } else {
	            pageable = PageRequest.of(page, size);
	        }
	    } else {
	        pageable = PageRequest.of(page, size);
	    }

	    Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);
	    if (orderPage == null || orderPage.getContent().isEmpty()) {
	        throw new OrderNotFoundException("No orders found for user with ID " + userId);
	    }

	    List<OrderDto> orderDtos = orderPage.getContent().stream()
	            .map(order -> modelMapper.map(order, OrderDto.class))
	            .collect(Collectors.toList());

	    OrderPageResponse response = new OrderPageResponse();
	    response.setContent(orderDtos);
	    response.setLastpage(orderPage.isLast());
	    response.setPageNumber(orderPage.getNumber());
	    response.setPageSize(orderPage.getSize());
	    response.setTotalElement(orderPage.getTotalElements());
	    response.setTotalPages(orderPage.getTotalPages());
	    return response;
	}


	@Override
	public OrderPageResponse getRecentOrders(Integer limit, Integer pageSize, Integer pageNumber,
	                                         String sortDir, String sortBy) throws OrderNotFoundException {
	    if (limit == null || limit <= 0) {
	        throw new IllegalArgumentException("Limit must be greater than zero.");
	    }

	    int page = pageNumber != null ? pageNumber : 0;
	    int size = pageSize != null ? pageSize : 10;

	    Pageable pageable = PageRequest.of(page, size,
	            sortDir != null && sortBy != null
	                    ? (sortDir.equalsIgnoreCase("asc")
	                            ? Sort.by(sortBy).ascending()
	                            : Sort.by(sortBy).descending())
	                    : Sort.unsorted());

	    Page<Order> orderPage = orderRepository.findAll(pageable);
	    if (orderPage == null || orderPage.getContent().isEmpty()) {
	        throw new OrderNotFoundException("No recent orders found.");
	    }

	    List<OrderDto> orderDTOs = orderPage.getContent().stream()
	            .limit(limit) 
	            .map(order -> modelMapper.map(order, OrderDto.class))
	            .collect(Collectors.toList());
	    OrderPageResponse response = new OrderPageResponse();
	    response.setContent(orderDTOs);
	    response.setLastpage(orderPage.isLast());
	    response.setPageNumber(orderPage.getNumber());
	    response.setPageSize(orderPage.getSize());
	    response.setTotalElement(orderPage.getTotalElements());
	    response.setTotalPages(orderPage.getTotalPages());
	    return response;

	}
}