package com.bookStore.SpringBootPractice.controller;

import com.bookStore.SpringBootPractice.appConstant.CartItemPageResponse;
import com.bookStore.SpringBootPractice.payloads.CartItemDto;
import com.bookStore.SpringBootPractice.exceptions.CartItemNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.CartNotFoundException;
import com.bookStore.SpringBootPractice.impl.CartItemServiceImpl;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemServiceImpl cartItemServiceImpl;

    public CartItemController(CartItemServiceImpl cartItemServiceImpl) {
        this.cartItemServiceImpl = cartItemServiceImpl;
    }

    @PostMapping("/{cartId}/add/{bookId}")
    public ResponseEntity<CartItemDto> addCartItem(
            @PathVariable Integer cartId,
            @PathVariable Integer bookId,
            @RequestBody CartItemDto cartItemDto,
            @AuthenticationPrincipal Principal principal) {
        try {
            CartItemDto addedCartItem = cartItemServiceImpl.addCartItem(cartId, cartItemDto, bookId);
            return new ResponseEntity<>(addedCartItem, HttpStatus.CREATED);
        } catch (CartNotFoundException | BookNotFoundException | SecurityException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable Integer cartItemId,
            @Valid @RequestBody CartItemDto cartItemDto,
            @AuthenticationPrincipal Principal principal) {
        try {
            CartItemDto updatedCartItem = cartItemServiceImpl.updateCartItem(cartItemId, cartItemDto);
            return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
        } catch (CartItemNotFoundException | SecurityException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Integer cartItemId,
            @AuthenticationPrincipal Principal principal) {
        try {
            cartItemServiceImpl.removeCartItem(cartItemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CartItemNotFoundException | SecurityException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<CartItemPageResponse> getItemsInCart(
            @PathVariable Integer cartId,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            CartItemPageResponse response = cartItemServiceImpl.getItemsInCart(cartId, pageNumber, pageSize, sortDir, sortBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemDto> getCartItem(@PathVariable Integer cartItemId) {
        try {
            CartItemDto cartItemDto = cartItemServiceImpl.getCartItem(cartItemId);
            return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
        } catch (CartItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<Void> clearCartItems(@PathVariable Integer cartId) {
        try {
            cartItemServiceImpl.clearCartItems(cartId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CartNotFoundException | CartItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartItemPageResponse> getCartItemByUserId(
            @PathVariable Integer userId,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam String sortBy,
            @RequestParam String sortDir) {
        try {
            CartItemPageResponse response = cartItemServiceImpl.getCartItemByUserId(userId, sortBy, sortDir, pageNumber, pageSize);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
