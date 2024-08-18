package com.bookStore.SpringBootPractice.service;
import com.bookStore.SpringBootPractice.appConstant.WishlistPageResponse;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.WishlistNotFoundOrNotExistException;
import com.bookStore.SpringBootPractice.payloads.WishlistDto;

public interface WishlistService {
    WishlistDto addToWishlist(Integer bookId) throws BookNotFoundException;
    void removeFromWishlist(Integer bookId) throws BookNotFoundException, WishlistNotFoundOrNotExistException;
    WishlistPageResponse getWishlist(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    boolean isBookInWishlist(Integer bookId) throws BookNotFoundException;
    void clearWishlist() throws WishlistNotFoundOrNotExistException;
    WishlistDto getWishlistByid(Integer id) throws WishlistNotFoundOrNotExistException;
    

}
