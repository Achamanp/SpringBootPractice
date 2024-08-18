package com.bookStore.SpringBootPractice.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.appConstant.BookPageResponse;
import com.bookStore.SpringBootPractice.entities.Author;
import com.bookStore.SpringBootPractice.entities.Book;
import com.bookStore.SpringBootPractice.entities.Review;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.entities.Wishlist;
import com.bookStore.SpringBootPractice.exceptions.AuthorNotExistOrNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.BookNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.ReviewNotFoundException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.BookDto;
import com.bookStore.SpringBootPractice.payloads.ReviewDto;
import com.bookStore.SpringBootPractice.repositories.AuthorRepository;
import com.bookStore.SpringBootPractice.repositories.BookRepository;
import com.bookStore.SpringBootPractice.repositories.OrderDetailRepository;
import com.bookStore.SpringBootPractice.repositories.OrderRepository;
import com.bookStore.SpringBootPractice.repositories.ReviewRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.repositories.WishlistRepository;
import com.bookStore.SpringBootPractice.service.BookService;
@Service
public class BookServiceImpl implements BookService{
	private AuthorRepository authorRepository;
	private BookRepository bookRepository;
	private ReviewRepository reviewRepository;
	private WishlistRepository wishlistRepository;
	private UserRepository userRepository;
	private ModelMapper modelMapper;

	

	public BookServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository,
			OrderDetailRepository orderDetailRepository, OrderRepository orderRepository,
			ReviewRepository reviewRepository, WishlistRepository wishlistRepository, ModelMapper modelMapper,UserRepository userRepository) {
		super();
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
		this.reviewRepository = reviewRepository;
		this.wishlistRepository = wishlistRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
	}

	@Override
	public BookDto addBook(BookDto bookDto, Integer authorId) throws AuthorNotExistOrNotFoundException {
	    if (bookDto == null) {
	        throw new NullPointerException("BookDto is required to add a new book. Please provide a valid BookDto object.");
	    }
	    if (authorId == null) {
	        throw new NullPointerException("Author id must not be null");
	    }
	    Author author = this.authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotExistOrNotFoundException("Author not found with id " + authorId));
	    Book book = this.modelMapper.map(bookDto, Book.class);
	    book.getAuthors().add(author);
	    Book savedBook = this.bookRepository.save(book);
	    return this.modelMapper.map(savedBook, BookDto.class);
	}
	@Override
	public BookDto updateBook(Integer id, BookDto updatedBook) throws BookNotFoundException  {
		Book book = this.bookRepository.findById(id).get();
		if(book==null) {
			throw new BookNotFoundException("No book found with the given ID " + id);
		}
		if(updatedBook==null) {
			throw new NullPointerException("BookDto is required to update a book. Please provide a valid BookDto object.");
		}
		book.setCategory(updatedBook.getCategory());
		book.setDescription(updatedBook.getDescription());
		book.setGenre(updatedBook.getGenre());
		book.setIsbn(updatedBook.getIsbn());
		book.setPrice(updatedBook.getPrice());
		book.setPublishers(updatedBook.getPublishers());
		book.setStockQuantity(updatedBook.getStockQuantity());
		book.setTittle(updatedBook.getTittle());
		book.setUpdatedAt(updatedBook.getUpdatedAt());
		Book updated = this.bookRepository.save(book);
		
		return this.modelMapper.map(updated, BookDto.class);
	}

	@Override
	public void deleteBook(Integer id) throws BookNotFoundException {
		Book book = this.bookRepository.findById(id).get();
		if(book==null) {
			throw new BookNotFoundException("No book found with the given ID " + id);
		}
		bookRepository.delete(book);
		
	}

	@Override
	public BookPageResponse searchBooksByTitle(String title, Integer pageNumber, Integer pageSize, String sortDir,
			String sortBy) throws BookNotFoundException {
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}else {
			sort = Sort.by(sortBy).descending();
		}
	    Pageable p = PageRequest.of(pageNumber,pageSize, sort);
	    Page<Book> pages = this.bookRepository.findByTittle(title,p);
	    if(pages == null) {
	    	throw new BookNotFoundException("Books does Not Exist with tittle " + title);
	    }
	    List<Book> allBooks = pages.getContent();
	    List<BookDto> bookDto = allBooks.stream().map(books-> 
	    this.modelMapper.map(books, BookDto.class)).collect(Collectors.toList());
	    BookPageResponse response = new BookPageResponse();
	    response.setContent(bookDto);
	    response.setLastpage(pages.isLast());
	    response.setPageNumber(pages.getNumber());
	    response.setPageSize(pages.getSize());
	    response.setTotalElement(pages.getTotalElements());
	    response.setTotalPages(pages.getTotalPages());
		return response;
	}

	@Override
	public BookPageResponse searchBooksByAuthor(String authorName, Integer pageNumber, Integer pageSize, String sortDir,
			String sortBy) throws BookNotFoundException {
		
			Sort sort = null;
			if(sortDir.equalsIgnoreCase("asc")) {
				sort = Sort.by(sortBy).ascending();
			}else {
				sort = Sort.by(sortBy).descending();
			}
		    Pageable p = PageRequest.of(pageNumber,pageSize, sort);
		    Page<Book> pages = this.authorRepository.findByName(authorName,p);
		    if(pages == null) {
		    	throw new BookNotFoundException("Books does Not Exist with authorname :" + authorName);
		    }
		    List<Book> allBooks = pages.getContent();
		    List<BookDto> bookDto = allBooks.stream().map(books-> 
		    this.modelMapper.map(books, BookDto.class)).collect(Collectors.toList());
		    BookPageResponse response = new BookPageResponse();
		    response.setContent(bookDto);
		    response.setLastpage(pages.isLast());
		    response.setPageNumber(pages.getNumber());
		    response.setPageSize(pages.getSize());
		    response.setTotalElement(pages.getTotalElements());
		    response.setTotalPages(pages.getTotalPages());
			return response;
	}

	@Override
	public BookPageResponse searchBookByIsbn(String isbn, Integer pageNumber, Integer pageSize, String sortDir,
			String sortBy) throws BookNotFoundException {
		
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}else {
			sort = Sort.by(sortBy).descending();
		}
	    Pageable p = PageRequest.of(pageNumber,pageSize, sort);
	    Page<Book> pages = this.bookRepository.findByIsbn(isbn,p);
	    if(pages == null) {
	    	throw new BookNotFoundException("Books does Not Exist with isbn " + isbn);
	    }
	    List<Book> allBooks = pages.getContent();
	    List<BookDto> bookDto = allBooks.stream().map(books-> 
	    this.modelMapper.map(books, BookDto.class)).collect(Collectors.toList());
	    BookPageResponse response = new BookPageResponse();
	    response.setContent(bookDto);
	    response.setLastpage(pages.isLast());
	    response.setPageNumber(pages.getNumber());
	    response.setPageSize(pages.getSize());
	    response.setTotalElement(pages.getTotalElements());
	    response.setTotalPages(pages.getTotalPages());
		return response;
	}

	@Override
	public List<BookDto> filterBooksByCategory(String category, Integer pageNumber, Integer pageSize, String sortDir,
			String sortBy) throws BookNotFoundException {
		 Pageable pageable = PageRequest.of(
		            pageNumber,
		            pageSize,
		            sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
		    );

		    Page<Book> booksPage = bookRepository.findByCategory(category, pageable);

		    if (booksPage.isEmpty()) {
		        throw new BookNotFoundException("No books found for the category: " + category);
		    }

		    List<BookDto> bookDtos = booksPage.getContent().stream()
		            .map(book -> modelMapper.map(book, BookDto.class))
		            .collect(Collectors.toList());

		    return bookDtos;
	}

	@Override
	public int getStock(Integer bookId) throws BookNotFoundException {
		Book book = this.bookRepository.findById(bookId).get();
		if(book==null) {
			throw new BookNotFoundException("Book not found with id " + bookId);
		}
	    Integer stock = book.getStockQuantity();
		return stock;
	}

	@Override
	public BookDto getBookDetails(Integer id) throws BookNotFoundException {
		Book book = this.bookRepository.findById(id).get();
		if(book==null) {
			throw new BookNotFoundException("Book not found with id " + id);
		}
		return this.modelMapper.map(book, BookDto.class);
	}
	@Override
	public ReviewDto addReview(Integer bookId, ReviewDto reviewDto) throws BookNotFoundException {
	    Book book = bookRepository.findById(bookId).get();
	    if(book == null) {
	    	throw new BookNotFoundException("Book not found with id " + bookId);
	    }
	    Review review = modelMapper.map(reviewDto, Review.class);
	    review.setBook(book);
	    User user = userRepository.findById(review.getUser().getId()).get();
	    if(user==null) {
	    	throw new UsernameNotFoundException("user Not found with id " + review.getUser().getId());
	    }   
	    review.setUser(user);
	    Review savedReview = reviewRepository.save(review);
	    return modelMapper.map(savedReview, ReviewDto.class);
	}


	@Override
	public ReviewDto updateReview(Integer reviewId, ReviewDto updatedReview) throws ReviewNotFoundException {
		Review review = this.reviewRepository.findById(reviewId).get();
		if(review== null) {
			throw new ReviewNotFoundException("Review not found with ID: " + reviewId);
		}
		review.setContent(updatedReview.getContent());
		review.setRating(updatedReview.getRating());
		Review UpdatedReview = this.reviewRepository.save(review);
		return this.modelMapper.map(UpdatedReview, ReviewDto.class);
	}

	@Override
	public void deleteReview(Integer reviewId) throws ReviewNotFoundException {
		Review review = this.reviewRepository.findById(reviewId).get();
		if(review== null) {
			throw new ReviewNotFoundException("Review not found with ID: " + reviewId);
		}
		this.reviewRepository.delete(review);	
	}

	@Override
	public List<ReviewDto> getReviews(Integer bookId) throws ReviewNotFoundException, BookNotFoundException {
	    if (bookId == null) {
	        throw new BookNotFoundException("Book ID is required");
	    }

	    Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId));

	    Set<Review> reviews =  book.getReviews();
	    if (reviews == null || reviews.isEmpty()) {
	        throw new ReviewNotFoundException("No reviews found for book with id " + bookId);
	    }

	    List<ReviewDto> reviewDtos = reviews.stream().map(review ->
	    this.modelMapper.map(review, ReviewDto.class)).collect(Collectors.toList());
	    return reviewDtos;
	}
	@Override
	public void addToWishlist(Integer userId, Integer bookId) throws BookNotFoundException, UserNotFoundException {
		if(userId == null ||bookId == null) {
			throw new NullPointerException("Book or UserId must not be null !!");
		}
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
	    
	    Book book = bookRepository.findById(bookId)
	            .orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId ));

	    boolean alreadyInWishlist = user.getWishlists().stream()
	            .anyMatch(wishlist -> wishlist.getBook().getId().equals(bookId));

	    if (alreadyInWishlist) {
	        return;
	    }

	    Wishlist wishlist = new Wishlist();
	    wishlist.setUser(user);
	    wishlist.setBook(book);

	    user.getWishlists().add(wishlist);
	    wishlistRepository.save(wishlist);
	    userRepository.save(user);
	}


	@Override
	public void removeFromWishlist(Integer userId, Integer bookId) throws UserNotFoundException, BookNotFoundException {
		if(userId == null ||bookId == null) {
			throw new NullPointerException("Book or UserId must not be null !!");
		}
		User user = this.userRepository.findById(userId).get();
		if(user==null) {
			throw new UserNotFoundException("User not found with id " + userId);
		}
		Book book = this.bookRepository.findById(bookId).get();
		if(book==null) {
			throw new BookNotFoundException("Book not found with id " + bookId);
		}

	    Wishlist wishlistItem = user.getWishlists().stream()
	            .filter(wishlist -> wishlist.getBook().getId().equals(bookId))
	            .findFirst()
	            .orElse(null);

	    if (wishlistItem != null) {
	        user.getWishlists().remove(wishlistItem);
	        wishlistRepository.delete(wishlistItem);
	        userRepository.save(user);
	    } else {
	        throw new BookNotFoundException("Book with id " + bookId + " not found in the user's wishlist");
	    }
		
	}

	@Override
	public List<BookDto> getWishlist(Integer userId, Integer pageNumber, Integer pageSize, String sortDir, String sortBy) throws UserNotFoundException {
	    if (userId == null) {
	        throw new IllegalArgumentException("User ID must not be null");
	    }

	    if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
	        throw new IllegalArgumentException("Sort direction must be 'asc' or 'desc'");
	    }

	    Pageable pageable = PageRequest.of(
	            pageNumber,
	            pageSize,
	            Sort.by(
	                sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
	                sortBy
	            )
	    );

	    Page<Book> booksPage = wishlistRepository.findBooksByUserId(userId, pageable);

	    if (booksPage.hasContent()) {
	        List<BookDto> bookDtos = booksPage.getContent().stream()
	                .map(book -> modelMapper.map(book, BookDto.class))
	                .collect(Collectors.toList());

	        return bookDtos;
	    } else {
	        return new ArrayList<>();
	    }
	}


	@Override
	public boolean isBookAvailable(Integer bookId) {
	    if (bookId == null) {
	        throw new IllegalArgumentException("Book ID must not be null");
	    }

	    return bookRepository.findById(bookId)
	            .map(Book::isAvailable)
	            .orElse(false);
	}

	@Override
	public void updateStock(Integer bookId, int quantity) throws BookNotFoundException {
	    if (bookId == null) {
	        throw new IllegalArgumentException("Book ID must not be null");
	    }

	    if (quantity < 0) {
	        throw new IllegalArgumentException("Quantity must be non-negative");
	    }

	    Book book = bookRepository.findById(bookId)
	            .orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId));

	    book.setStockQuantity(quantity);
	    bookRepository.save(book);
	}

}
