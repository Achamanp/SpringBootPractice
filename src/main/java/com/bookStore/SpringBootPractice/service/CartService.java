package com.bookStore.SpringBootPractice.service;

import com.bookStore.SpringBootPractice.appConstant.CartPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.CartNotFoundException;
import com.bookStore.SpringBootPractice.payloads.CartDto;

public interface CartService {
    CartDto createCart(CartDto cartDto) throws BookNotFoundException;
    CartDto getCart();
    void clearCart();
    double calculateCartTotal(Integer cartId) throws CartNotFoundException;
    CartDto saveCartForLater(Integer cartId, Integer userId) throws CartNotFoundException;
    CartPageResponse getCartByUser(Integer userId,Integer pageSize, Integer pageNumber, String sortDir, String sortBy);
    CartPageResponse getAllCart(Integer pageSize, Integer pageNumber, String sortDir, String sortBy);
    
}
