package com.bookStore.SpringBootPractice.payloads;

import java.time.LocalDate;
import java.util.List;


public class OrderDto {
	 private Integer id;
	    private String orderNumber;
	    private LocalDate orderDate = LocalDate.now();
	    private LocalDate updatedAt = LocalDate.now();
	    private Double totalAmount;
	    private UserDto user;
	    private String comment;
	    private String paymentMethod;
	    private String status;
	    private List<OrderDetailDto> orderDetails;
	    enum status{
	    	PLACED,SHIPPED,DELIVERED,CANCELED;
	    }
	    private String shippingAddress;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public LocalDate getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(LocalDate orderDate) {
			this.orderDate = orderDate;
		}
		public Double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
		public UserDto getUser() {
			return user;
		}
		public void setUser(UserDto user) {
			this.user = user;
		}
		public List<OrderDetailDto> getOrderDetails() {
			return orderDetails;
		}
		public void setOrderDetails(List<OrderDetailDto> orderDetails) {
			this.orderDetails = orderDetails;
		}
		public String getShippingAddress() {
			return shippingAddress;
		}
		public void setShippingAddress(String shippingAddress) {
			this.shippingAddress = shippingAddress;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public String getPaymentMethod() {
			return paymentMethod;
		}
		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}   
		
}
