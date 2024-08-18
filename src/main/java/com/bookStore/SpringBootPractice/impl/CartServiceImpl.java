package com.bookStore.SpringBootPractice.impl;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.appConstant.CartPageResponse;
import com.bookStore.SpringBootPractice.entities.Cart;
import com.bookStore.SpringBootPractice.entities.CartItem;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.exceptions.CartNotFoundException;
import com.bookStore.SpringBootPractice.payloads.CartDto;
import com.bookStore.SpringBootPractice.repositories.BookRepository;
import com.bookStore.SpringBootPractice.repositories.CartItemRepository;
import com.bookStore.SpringBootPractice.repositories.CartRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.service.CartService;
@Service
public class CartServiceImpl implements CartService{
	private UserRepository userRepository;
	private CartRepository cartRepository;
	private CartItemRepository cartItemRepository;
	private Principal principal;
	private BookRepository bookRepository;
	private ModelMapper modelMapper;
	public CartServiceImpl(UserRepository userRepository,CartRepository
			cartRepository,CartItemRepository cartItemRepository
			,ModelMapper modelMapper, BookRepository bookRepository) {
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
		this.cartRepository = cartRepository;
		this.modelMapper = modelMapper;
	}
	@Override
	public CartDto createCart(CartDto cartDto) {
		String username = this.principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		Cart cart = this.modelMapper.map(cartDto, Cart.class);
		if(!user.getId().equals(cart.getUser().getId())) {
			throw new SecurityException("You are not authorized to delete this review.");
		}
        cart.setUser(user);
        Cart saveCart = this.cartRepository.save(cart);
		return this.modelMapper.map(saveCart, CartDto.class);
	}

	@Override
	public CartDto getCart() {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		Integer userId = user.getId();
		Cart cart = this.cartRepository.findByUserId(userId);
		return this.modelMapper.map(cart, CartDto.class);
	}

	@Override
	public void clearCart() {
		String username = this.principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		Integer userId = user.getId();
		List<Cart> cart = this.cartRepository.findAllByUserId(userId);
		this.cartRepository.deleteAll(cart);
		
	}

	  @Override
	    public double calculateCartTotal(Integer cartId) throws CartNotFoundException {
	        Cart cart = cartRepository.findById(cartId)
	                .orElseThrow(() -> new CartNotFoundException("Cart with ID " + cartId + " not found"));
	        Set<CartItem> cartItems = cart.getItem();
	        if (cartItems.isEmpty()) {
	            return 0.0;
	        }
	        double total = cartItems.stream()
	                .mapToDouble(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
	                .sum();

	        return total;
	    }

	  @Override
	  public CartDto saveCartForLater(Integer cartId, Integer userId) throws CartNotFoundException {
	      Cart cart = cartRepository.findById(cartId)
	              .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));
	      if (!cart.getUser().getId().equals(userId)) {
	          throw new SecurityException("User is not authorized to save this cart for later.");
	      }
	      cart.setSavedForLater(true);
	      Cart savedCart = cartRepository.save(cart);
	      CartDto cartDto = modelMapper.map(savedCart, CartDto.class);
	      return cartDto;
	  }
	@Override
	public CartPageResponse getAllCart(Integer pageSize, Integer pageNumber, String sortDir, String sortBy) {
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}else sort = Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Cart> pages = this.cartRepository.findAll(p);
		List<Cart> cart = pages.getContent();
		List<CartDto> cartDto = cart.stream().map(dto-> 
		this.modelMapper.map(dto, CartDto.class)).collect(Collectors.toList());
		CartPageResponse response = new CartPageResponse();
		response.setContent(cartDto);
		response.setLastpage(pages.isLast());
		response.setPageNumber(pages.getNumber());
		response.setPageSize(pages.getSize());
		response.setTotalElement(pages.getTotalElements());
		response.setTotalPages(pages.getTotalPages());
		return response;
	}
	@Override
	public CartPageResponse getCartByUser(Integer userId, Integer pageSize, Integer pageNumber, String sortDir,
			String sortBy) {
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}else sort = Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Cart> pages = this.cartRepository.findByUserId(userId,p);
		List<Cart> cart = pages.getContent();
		List<CartDto> cartDto = cart.stream().map(dto-> 
		this.modelMapper.map(dto, CartDto.class)).collect(Collectors.toList());
		CartPageResponse response = new CartPageResponse();
		response.setContent(cartDto);
		response.setLastpage(pages.isLast());
		response.setPageNumber(pages.getNumber());
		response.setPageSize(pages.getSize());
		response.setTotalElement(pages.getTotalElements());
		response.setTotalPages(pages.getTotalPages());
		return response;
	}

}
