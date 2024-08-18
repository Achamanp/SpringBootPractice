package com.bookStore.SpringBootPractice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookStore.SpringBootPractice.payloads.OrderDetailDto;
import com.bookStore.SpringBootPractice.appConstant.OrderDetailPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.OrderDetailNotFoundOrNotExistException;
import com.bookStore.SpringBootPractice.exceptions.OrderNotFoundException;
import com.bookStore.SpringBootPractice.impl.OrderDetailServiceImpl;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private OrderDetailServiceImpl orderDetailServiceImpl;

    public OrderDetailController(OrderDetailServiceImpl orderDetailServiceImpl) {
        this.orderDetailServiceImpl = orderDetailServiceImpl;
    }

    @PostMapping("/{bookId}/order/{orderId}")
    public ResponseEntity<OrderDetailDto> createOrderDetail(
            @RequestBody OrderDetailDto orderDetailDto,
            @PathVariable Integer bookId,
            @PathVariable Integer orderId) {
        try {
            OrderDetailDto createdOrderDetail = this.orderDetailServiceImpl.createOrderDetail(orderDetailDto, bookId, orderId);
            return new ResponseEntity<>(createdOrderDetail, HttpStatus.CREATED);
        } catch (OrderNotFoundException | BookNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{orderDetailId}")
    public ResponseEntity<OrderDetailDto> updateOrderDetail(
            @RequestBody OrderDetailDto orderDetailDto,
            @PathVariable Integer orderDetailId) {
        try {
            OrderDetailDto updatedOrderDetail = this.orderDetailServiceImpl.updateOrderDetail(orderDetailDto, orderDetailId);
            return new ResponseEntity<>(updatedOrderDetail, HttpStatus.OK);
        } catch (OrderDetailNotFoundOrNotExistException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Integer orderDetailId) {
        try {
            orderDetailServiceImpl.deleteOrderDetail(orderDetailId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OrderDetailNotFoundOrNotExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<OrderDetailDto> getOrderDetailById(@PathVariable Integer orderDetailId) {
        try {
            OrderDetailDto orderDetailDto = orderDetailServiceImpl.getOrderDetailById(orderDetailId);
            return new ResponseEntity<>(orderDetailDto, HttpStatus.OK);
        } catch (OrderDetailNotFoundOrNotExistException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDetailPageResponse> getOrderDetailsByOrderId(
            @PathVariable Integer orderId,
            @RequestParam(defaultValue = "0") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            OrderDetailPageResponse response = orderDetailServiceImpl.getOrderDetailsByOrderId(orderId, pageSize, pageNumber, sortBy, sortDir);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (OrderDetailNotFoundOrNotExistException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<OrderDetailPageResponse> getOrderDetailsByBookId(
            @PathVariable Integer bookId,
            @RequestParam(defaultValue = "0") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            OrderDetailPageResponse response = orderDetailServiceImpl.getOrderDetailsByBookId(bookId, pageSize, pageNumber, sortBy, sortDir);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<OrderDetailPageResponse> getAllOrderDetails(
            @RequestParam(defaultValue = "0") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            OrderDetailPageResponse response = orderDetailServiceImpl.getOrderDetails(pageSize, pageNumber, sortBy, sortDir);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/order/{orderId}/total-price")
    public ResponseEntity<Double> calculateTotalPriceForOrder(@PathVariable Integer orderId) {
        try {
            Double totalPrice = orderDetailServiceImpl.calculateTotalPriceForOrder(orderId);
            return new ResponseEntity<>(totalPrice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/order/{orderId}/check-book-availability")
    public ResponseEntity<Boolean> checkBookAvailabilityInOrder(@PathVariable Integer orderId) {
        try {
            boolean isAvailable = orderDetailServiceImpl.checkBookAvailabilityInOrder(orderId);
            return new ResponseEntity<>(isAvailable, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
