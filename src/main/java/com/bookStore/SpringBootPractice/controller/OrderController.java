package com.bookStore.SpringBootPractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.bookStore.SpringBootPractice.payloads.ApiResponse;
import com.bookStore.SpringBootPractice.payloads.OrderDto;
import com.bookStore.SpringBootPractice.appConstant.OrderPageResponse;
import com.bookStore.SpringBootPractice.exceptions.OrderNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.OrderProcessingException;
import com.bookStore.SpringBootPractice.exceptions.ResourceNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.impl.OrderServiceImpl;



@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws OrderProcessingException, UserNotFoundException {
        OrderDto createdOrder = orderServiceImpl.createOrder(orderDto);
		return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder( @RequestBody OrderDto orderDto) throws OrderNotFoundException, OrderProcessingException, UserNotFoundException {
        try {
            OrderDto updatedOrder = orderServiceImpl.updateOrder(orderDto);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Integer orderId) throws OrderNotFoundException, UserNotFoundException {
        try {
            orderServiceImpl.deleteOrder(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            ApiResponse Response = new ApiResponse(false, "Order not found");
            return new ResponseEntity<>(Response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Integer orderId) throws OrderNotFoundException, UserNotFoundException {
        try {
            OrderDto orderDto = orderServiceImpl.getOrderById(orderId);
            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<OrderPageResponse> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "direction", defaultValue = "desc") String direction) {
        OrderPageResponse orderPageResponse = orderServiceImpl.getAllOrders(page, size, sortBy, direction);
        return new ResponseEntity<>(orderPageResponse, HttpStatus.OK);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<OrderPageResponse> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "sortDir", required = false) String sortDir,
            @RequestParam(value = "sortBy", required = false) String sortBy) {

        try {
            OrderPageResponse response = orderServiceImpl.getOrdersByStatus(status, pageSize, pageNumber, sortDir, sortBy);
            return ResponseEntity.ok(response);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<OrderPageResponse>(HttpStatus.NOT_FOUND);
            		
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<OrderPageResponse> getOrdersByUserId(
            @PathVariable Integer userId,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        
        try {
            OrderPageResponse response = orderServiceImpl.getOrdersByUserId(userId, pageSize, pageNumber, sortDir, sortBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<OrderPageResponse>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<OrderPageResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<OrderPageResponse> getOrdersByDateRange(
            @RequestParam(name = "start_date") String startDate,
            @RequestParam(name = "end_date") String endDate,
            @RequestParam(name = "page_size", required = false) Integer pageSize,
            @RequestParam(name = "page_number", required = false) Integer pageNumber,
            @RequestParam(name = "sort_dir", required = false) String sortDir,
            @RequestParam(name = "sort_by", required = false) String sortBy) {
        
        try {
            OrderPageResponse response = orderServiceImpl.getOrdersByDateRange(startDate, endDate, pageSize, pageNumber, sortDir, sortBy);
            return ResponseEntity.ok(response);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<OrderPageResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        @GetMapping("/by-payment-method")
        public ResponseEntity<OrderPageResponse> getOrdersByPaymentMethod(
                @RequestParam(name = "payment_method") String paymentMethod,
                @RequestParam(name = "page_size", required = false) Integer pageSize,
                @RequestParam(name = "page_number", required = false) Integer pageNumber,
                @RequestParam(name = "sort_dir", required = false) String sortDir,
                @RequestParam(name = "sort_by", required = false) String sortBy) {
            
            try {
                OrderPageResponse response = orderServiceImpl.getOrdersByPaymentMethod(paymentMethod, pageSize, pageNumber, sortDir, sortBy);
                return ResponseEntity.ok(response);
            } catch (OrderNotFoundException e) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    

        @GetMapping("/by-total-amount-range")
        public ResponseEntity<OrderPageResponse> getOrdersByTotalAmountRange(
                @RequestParam(name = "min_amount") Double minAmount,
                @RequestParam(name = "max_amount") Double maxAmount,
                @RequestParam(name = "page_size", required = false) Integer pageSize,
                @RequestParam(name = "page_number", required = false) Integer pageNumber,
                @RequestParam(name = "sort_dir", required = false) String sortDir,
                @RequestParam(name = "sort_by", required = false) String sortBy) {
            
            try {
                OrderPageResponse response = orderServiceImpl.getOrdersByTotalAmountRange(minAmount, maxAmount, pageSize, pageNumber, sortDir, sortBy);
                return ResponseEntity.ok(response);
            } catch (OrderNotFoundException e) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        @GetMapping("/exists/{id}")
        public ResponseEntity<Boolean> orderExists(@PathVariable Integer id) {
            try {
                boolean exists = orderServiceImpl.orderExist(id);
                return ResponseEntity.ok(exists);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

            @PatchMapping("/{id}/status")
            public ResponseEntity<OrderDto> processOrder(@PathVariable Integer id, @RequestBody String status) {
                try {
                    OrderDto orderDto = orderServiceImpl.processOrder(id, status);
                    return ResponseEntity.ok(orderDto);
                } catch (IllegalArgumentException e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.BAD_REQUEST);
                } catch (OrderNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            @PutMapping("/{id}/comment")
            public ResponseEntity<OrderDto> addCommentToOrder(@PathVariable Integer id, @RequestBody String comment) {
                try {
                    OrderDto updatedOrder = orderServiceImpl.addCommentToOrder(id, comment);
                    return ResponseEntity.ok(updatedOrder);
                } catch (OrderNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (IllegalArgumentException e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.BAD_REQUEST);
                } catch (Exception e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @PutMapping("/{id}/shipping-address")
            public ResponseEntity<OrderDto> updateShippingAddress(@PathVariable Integer id, @RequestBody String newAddress) {
                try {
                    OrderDto updatedOrder = orderServiceImpl.updateShippingAddress(id, newAddress);
                    return ResponseEntity.ok(updatedOrder);
                } catch (OrderNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (IllegalArgumentException e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.BAD_REQUEST);
                } catch (Exception e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.INTERNAL_SERVER_ERROR);
          
                }
            }

            @PutMapping("/{id}/cancel")
            public ResponseEntity<OrderDto> cancelOrder(@PathVariable Integer id) {
                try {
                    OrderDto updatedOrder = orderServiceImpl.cancelOrder(id);
                    return ResponseEntity.ok(updatedOrder);
                } catch (OrderNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (IllegalStateException e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.BAD_REQUEST);
                } catch (Exception e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @PostMapping("/{id}/reorder")
            public ResponseEntity<OrderDto> reorder(@PathVariable Integer id) {
                try {
                    OrderDto newOrder = orderServiceImpl.reorder(id);
                    return ResponseEntity.ok(newOrder);
                } catch (OrderNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (Exception e) {
                    return new ResponseEntity<OrderDto>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @GetMapping("/history/{userId}")
            public ResponseEntity<OrderPageResponse> getOrderHistoryByCustomer(@PathVariable Integer userId,
                                                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                @RequestParam(defaultValue = "0") Integer pageNumber,
                                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                                @RequestParam(defaultValue = "desc") String sortDir) {
                try {
                    OrderPageResponse response = orderServiceImpl.getOrderHistoryByCustomer(userId, pageSize, pageNumber, sortDir, sortBy);
                    return ResponseEntity.ok(response);
                } catch (OrderNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (IllegalArgumentException e) {
                    return new  ResponseEntity<OrderPageResponse>(HttpStatus.BAD_REQUEST);
                } catch (Exception e) {
                    return new ResponseEntity<OrderPageResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @GetMapping("/recent")
            public ResponseEntity<OrderPageResponse> getRecentOrders(@RequestParam(defaultValue = "10") Integer limit,
                                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                                     @RequestParam(defaultValue = "0") Integer pageNumber,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "desc") String sortDir) {
                try {
                    OrderPageResponse response = orderServiceImpl.getRecentOrders(limit, pageSize, pageNumber, sortDir, sortBy);
                    return ResponseEntity.ok(response);
                } catch (OrderNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (IllegalArgumentException e) {
                    return  new  ResponseEntity<OrderPageResponse>(HttpStatus.BAD_REQUEST);
                } catch (Exception e) {
                    return new ResponseEntity<OrderPageResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
       
}
