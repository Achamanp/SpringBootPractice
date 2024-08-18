package com.bookStore.SpringBootPractice.payloads;



public class CartItemDto {
	private Integer id;
	private BookDto book ;
	private Integer quantity;
	private double price;
	private CartDto cart;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BookDto getBook() {
		return book;
	}
	public void setBook(BookDto book) {
		this.book = book;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public CartDto getCart() {
		return cart;
	}
	public void setCart(CartDto cart) {
		this.cart = cart;
	}
	
}
