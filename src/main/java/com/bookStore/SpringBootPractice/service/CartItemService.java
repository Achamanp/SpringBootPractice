package com.bookStore.SpringBootPractice.service;


import com.bookStore.SpringBootPractice.appConstant.CartItemPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.CartItemNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.CartNotFoundException;
import com.bookStore.SpringBootPractice.payloads.CartItemDto;

public interface CartItemService {
    CartItemDto addCartItem(Integer cartId, CartItemDto cartItemDto,Integer bookId) throws CartNotFoundException, BookNotFoundException;
    CartItemDto updateCartItem(Integer cartItemId, CartItemDto cartItemDto) throws CartItemNotFoundException;
    void removeCartItem(Integer cartItemId) throws CartItemNotFoundException;
    CartItemPageResponse getItemsInCart(Integer cartId,Integer pageNumber , Integer pgeSize, String sortDir, String sortBy) throws CartNotFoundException;
    CartItemDto getCartItem(Integer cartItemId) throws CartItemNotFoundException;
    void clearCartItems(Integer cartId) throws CartNotFoundException, CartItemNotFoundException;
    CartItemDto updateCartItemQuantity(Integer cartItemId, Integer quantity) throws CartItemNotFoundException;
    CartItemPageResponse getCartItemByUserId(Integer userId, String sortBy, String sortDir, Integer pageNumber, Integer pageSize);
}
