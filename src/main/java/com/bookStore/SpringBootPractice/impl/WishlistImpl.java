package com.bookStore.SpringBootPractice.impl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import com.bookStore.SpringBootPractice.entities.*;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.WishlistNotFoundOrNotExistException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.appConstant.WishlistPageResponse;
import com.bookStore.SpringBootPractice.payloads.WishlistDto;
import com.bookStore.SpringBootPractice.repositories.BookRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.repositories.WishlistRepository;
import com.bookStore.SpringBootPractice.service.WishlistService;
@Service
public class WishlistImpl implements WishlistService{
    private ModelMapper modelMapper;
    private WishlistRepository wishlistRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private Principal principal;
    
	public WishlistImpl(ModelMapper modelMapper, WishlistRepository wishlistRepository, UserRepository userRepository,
			BookRepository bookRepository) {
		super();
		this.modelMapper = modelMapper;
		this.wishlistRepository = wishlistRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
	}

	@Override
	public WishlistDto addToWishlist(Integer bookId) throws BookNotFoundException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).orElseThrow(()-> 
		new UsernameNotFoundException("User not foudn with username " +username));
		Book book = this.bookRepository.findById(bookId).orElseThrow(()->
		new BookNotFoundException("Book not found with id " + bookId));
		Wishlist wishlist = new Wishlist();
		wishlist.setBook(book);
		wishlist.setUser(user);
		Wishlist saveWishlist = this.wishlistRepository.save(wishlist);
		return this.modelMapper.map(saveWishlist, WishlistDto.class);
	}

	@Override
	public void removeFromWishlist(Integer bookId) throws BookNotFoundException, WishlistNotFoundOrNotExistException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).orElseThrow(()-> 
		new UsernameNotFoundException("User not foudn with username " +username));
		Wishlist wishlist = this.wishlistRepository.findByBookId(bookId);
		if(wishlist==null) {
			throw new WishlistNotFoundOrNotExistException("Wishlist not found or not created yet of bookId " +bookId);
		}
		if(!user.getId().equals(wishlist.getUser().getId())) {
			throw new SecurityException("You are not authorized to delete this wishlist.");
		}
		this.wishlistRepository.delete(wishlist);
	}

	@Override
	public WishlistPageResponse getWishlist(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}else {
			sort = Sort.by(sortBy).descending();
		}
		Pageable p = PageRequest.of(pageSize, pageNumber, sort);
		Page<Wishlist> pages = wishlistRepository.findAll(p);
		List<Wishlist> wishlist = pages.getContent();
		List<WishlistDto> wishlistDto = wishlist.stream().map(dto->
		this.modelMapper.map(dto, WishlistDto.class)).collect(Collectors.toList());
		WishlistPageResponse response = new WishlistPageResponse();
		response.setContent(wishlistDto);
		response.setLastpage(pages.isLast());
		response.setPageNumber(pages.getNumber());
		response.setPageSize(pages.getSize());
		response.setTotalElement(pages.getTotalElements());
		response.setTotalPages(pages.getTotalPages());
		return response;
	}

	@Override
	public boolean isBookInWishlist(Integer bookId) throws BookNotFoundException {
		Book book = this.bookRepository.findById(bookId).orElseThrow(()->
		new BookNotFoundException(" book not found or not exist with id " + bookId));
	    String username = principal.getName();
	    User user = this.userRepository.findByUsername(username).get();
	    Wishlist wishlist = this.wishlistRepository.findByUserId(user.getId());
	    if(wishlist.getBook()==book) {
	    	return true;
	    }
		return false;
	}

	@Override
	public void clearWishlist() throws WishlistNotFoundOrNotExistException {
	    String username = principal.getName();
	    User user = userRepository.findByUsername(username).get();
	    Integer userId = user.getId();
	    List<Wishlist> wishlistItems = wishlistRepository.findWishlistsByUserId(userId);
	    if (wishlistItems.isEmpty()) {
	        throw new WishlistNotFoundOrNotExistException("No items found in the wishlist for user with ID: " + userId);
	    }
	    
	    wishlistRepository.deleteAll(wishlistItems);
	}

	@Override
	public WishlistDto getWishlistByid(Integer id) throws WishlistNotFoundOrNotExistException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		Wishlist wishlist = this.wishlistRepository.findById(id).orElseThrow(()-> 
		new WishlistNotFoundOrNotExistException("Wishlist not found or not exist with id " + id));
		if(!user.getId().equals(wishlist.getUser().getId())) {
			throw new SecurityException("You are not authorized to fetch this wishlist.");
		}
		return this.modelMapper.map(wishlist, WishlistDto.class);
	}
}
