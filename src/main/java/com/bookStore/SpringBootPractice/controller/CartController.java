package com.bookStore.SpringBootPractice.controller;

import com.bookStore.SpringBootPractice.appConstant.CartPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.CartNotFoundException;
import com.bookStore.SpringBootPractice.impl.CartServiceImpl;
import com.bookStore.SpringBootPractice.payloads.CartDto;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartServiceImpl cartServiceImpl;

    public CartController(CartServiceImpl cartServiceImpl) {
        this.cartServiceImpl = cartServiceImpl;
    }

    @PostMapping("/create")
    public ResponseEntity<CartDto> createCart(@Valid @RequestBody CartDto cartDto, Principal principal) throws BookNotFoundException {
        try {
            CartDto createdCart = cartServiceImpl.createCart(cartDto);
            return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<CartDto> getCart(Principal principal) {
        try {
            CartDto cartDto = cartServiceImpl.getCart();
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Principal principal) {
        try {
            cartServiceImpl.clearCart();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/total/{cartId}")
    public ResponseEntity<Double> calculateCartTotal(@PathVariable Integer cartId) {
        try {
            double total = cartServiceImpl.calculateCartTotal(cartId);
            return new ResponseEntity<>(total, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/saveForLater/{cartId}/{userId}")
    public ResponseEntity<CartDto> saveCartForLater(@PathVariable Integer cartId, @PathVariable Integer userId) {
        try {
            CartDto cartDto = cartServiceImpl.saveCartForLater(cartId, userId);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        } catch (CartNotFoundException | SecurityException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<CartPageResponse> getAllCart(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            CartPageResponse response = cartServiceImpl.getAllCart(pageSize, pageNumber, sortDir, sortBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartPageResponse> getCartByUser(
            @PathVariable Integer userId,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber,
            @RequestParam String sortDir,
            @RequestParam String sortBy) {
        try {
            CartPageResponse response = cartServiceImpl.getCartByUser(userId, pageSize, pageNumber, sortDir, sortBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
