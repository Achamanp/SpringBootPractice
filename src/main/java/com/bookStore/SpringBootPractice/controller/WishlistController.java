package com.bookStore.SpringBootPractice.controller;

import com.bookStore.SpringBootPractice.payloads.WishlistDto;
import com.bookStore.SpringBootPractice.appConstant.WishlistPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.WishlistNotFoundOrNotExistException;
import com.bookStore.SpringBootPractice.impl.WishlistImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistImpl wishlistImpl;

    public WishlistController(WishlistImpl wishlistImpl) {
        this.wishlistImpl = wishlistImpl;
    }

    @PostMapping("/add/{bookId}")
    public ResponseEntity<WishlistDto> addToWishlist(
            @PathVariable Integer bookId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            WishlistDto wishlistDto = wishlistImpl.addToWishlist(bookId);
            return new ResponseEntity<>(wishlistDto, HttpStatus.CREATED);
        } catch (BookNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Integer bookId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            wishlistImpl.removeFromWishlist(bookId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookNotFoundException | WishlistNotFoundOrNotExistException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SecurityException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<WishlistPageResponse> getWishlist(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            WishlistPageResponse response = wishlistImpl.getWishlist(pageNumber, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/check/{bookId}")
    public ResponseEntity<Boolean> isBookInWishlist(@PathVariable Integer bookId) {
        try {
            boolean exists = wishlistImpl.isBookInWishlist(bookId);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (BookNotFoundException ex) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @DeleteMapping("/clear")
//    public ResponseEntity<Void> clearWishlist(@AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            wishlistImpl.clearWishlist();
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (WishlistNotFoundOrNotExistException ex) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistDto> getWishlistById(@PathVariable Integer id) {
        try {
            WishlistDto wishlistDto = wishlistImpl.getWishlistByid(id);
            return new ResponseEntity<>(wishlistDto, HttpStatus.OK);
        } catch (WishlistNotFoundOrNotExistException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (SecurityException ex) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
