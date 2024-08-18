package com.bookStore.SpringBootPractice.payloads;

public class OrderDetailDto {
	 private Integer id;
	    private Integer quantity;
	    private Double price;
	    private OrderDto order;
	    private BookDto book;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public OrderDto getOrder() {
			return order;
		}
		public void setOrder(OrderDto order) {
			this.order = order;
		}
		public BookDto getBook() {
			return book;
		}
		public void setBook(BookDto book) {
			this.book = book;
		}
	    
}
