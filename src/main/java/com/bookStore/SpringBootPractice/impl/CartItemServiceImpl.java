package com.bookStore.SpringBootPractice.impl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.appConstant.CartItemPageResponse;
import com.bookStore.SpringBootPractice.appConstant.CartPageResponse;
import com.bookStore.SpringBootPractice.entities.Book;
import com.bookStore.SpringBootPractice.entities.Cart;
import com.bookStore.SpringBootPractice.entities.CartItem;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.CartItemNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.CartNotFoundException;
import com.bookStore.SpringBootPractice.payloads.CartItemDto;
import com.bookStore.SpringBootPractice.repositories.BookRepository;
import com.bookStore.SpringBootPractice.repositories.CartItemRepository;
import com.bookStore.SpringBootPractice.repositories.CartRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.service.CartItemService;
@Service
public class CartItemServiceImpl implements CartItemService{
	private ModelMapper modelMapper;
	private UserRepository userRepository;
	private CartItemRepository cartItemRepository;
	private CartRepository cartRepository;
	private Principal principal;
	private BookRepository bookRepository;

	public CartItemServiceImpl(ModelMapper modelMapper, UserRepository userRepository,
			CartItemRepository cartItemRepository, CartRepository cartRepository
			,BookRepository bookRepository) {
		super();
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
		this.cartRepository = cartRepository;
		this.bookRepository = bookRepository;
	}

	@Override
	public CartItemDto addCartItem(Integer cartId, CartItemDto cartItemDto,Integer bookId) throws CartNotFoundException, BookNotFoundException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException("The cart you are searching for that not exist"));
		Book book = this.bookRepository.findById(bookId).orElseThrow(()->
		new BookNotFoundException("Book not found or not exist with id " + bookId));
		if(!user.getId().equals(cart.getUser().getId())) {
			throw new  SecurityException("You are not authenticated to create cart item for the user");
		}
		CartItem cartItem = this.modelMapper.map(cartItemDto, CartItem.class);
		cartItem.setCart(cart);
		cartItem.setBook(book);
		return this.modelMapper.map(cartItem, CartItemDto.class);
	}

	@Override
	public CartItemDto updateCartItem(Integer cartItemId, CartItemDto cartItemDto) throws CartItemNotFoundException {
		if(cartItemDto==null || cartItemId==null) {
			throw new NullPointerException("CartItemDto or cartItemId can not be null ");
		}
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		CartItem cartItem = this.cartItemRepository.findById(cartItemId).orElseThrow(()-> new CartItemNotFoundException("CartItem not found with id " + cartItemId));
		if(!user.getId().equals(cartItem.getCart().getUser().getId())) {
			throw new SecurityException("you are not allow to update the cartItem with id " + cartItemId);
		}
		cartItem.setPrice(cartItemDto.getPrice());
		cartItem.setQuantity(cartItemDto.getQuantity());
		CartItem updatedCartItem = this.cartItemRepository.save(cartItem);
		return this.modelMapper.map(updatedCartItem, null);
	}

	@Override
	public void removeCartItem(Integer cartItemId) throws CartItemNotFoundException {
		String username = principal.getName();
		User user = this.userRepository.findByUsername(username).get();
		CartItem cartItem = this.cartItemRepository.findById(cartItemId).orElseThrow(()-> 
		new CartItemNotFoundException("CartItem not found with id " + cartItemId));
		if(!user.getId().equals(cartItem.getCart().getUser().getId())) {
			throw new SecurityException("you are not allow to delete this cartItem");
		}
		this.cartItemRepository.delete(cartItem);
		
	}


    @Override
    public CartItemPageResponse getItemsInCart(Integer cartId, Integer pageNumber, Integer pageSize, String sortDir, String sortBy) throws CartNotFoundException {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<CartItem> cartItemPage = cartItemRepository.findByCartId(cartId, pageable);

        List<CartItemDto> cartItemDtos = cartItemPage.getContent().stream()
                .map(item -> modelMapper.map(item, CartItemDto.class))
                .collect(Collectors.toList());

        CartItemPageResponse response = new CartItemPageResponse();
        response.setContent(cartItemDtos);
        response.setLastpage(cartItemPage.isLast());
        response.setPageNumber(cartItemPage.getNumber());
        response.setPageSize(cartItemPage.getSize());
        response.setTotalElement(cartItemPage.getTotalElements());
        response.setTotalPages(cartItemPage.getTotalPages());

        return response;
    }

    @Override
    public CartItemDto getCartItem(Integer cartItemId) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with ID: " + cartItemId));
        
        return modelMapper.map(cartItem, CartItemDto.class);
    }

    @Override
    public void clearCartItems(Integer cartId) throws CartNotFoundException, CartItemNotFoundException {
        
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new CartItemNotFoundException("No items found in cart with ID: " + cartId);
        }
        
        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    public CartItemDto updateCartItemQuantity(Integer cartItemId, Integer quantity) throws CartItemNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with ID: " + cartItemId));
        
        cartItem.setQuantity(quantity);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        return modelMapper.map(updatedCartItem, CartItemDto.class);
    }

    @Override
    public CartItemPageResponse getCartItemByUserId(Integer userId, String sortBy, String sortDir, Integer pageNumber, Integer pageSize) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<CartItem> cartItemPage = cartItemRepository.findByUserId(userId, pageable);

        List<CartItemDto> cartItemDtos = cartItemPage.getContent().stream()
                .map(item -> modelMapper.map(item, CartItemDto.class))
                .collect(Collectors.toList());

        CartItemPageResponse response = new CartItemPageResponse();
        response.setContent(cartItemDtos);
        response.setLastpage(cartItemPage.isLast());
        response.setPageNumber(cartItemPage.getNumber());
        response.setPageSize(cartItemPage.getSize());
        response.setTotalElement(cartItemPage.getTotalElements());
        response.setTotalPages(cartItemPage.getTotalPages());

        return response;
    }

}
