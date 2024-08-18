package com.bookStore.SpringBootPractice.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalException {
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorDetail> userNotFoundExceptionHandeler(UserNotFoundException e ,WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<ErrorDetail> otpInvalidExceptionHandeler(InvalidOtpException ex, WebRequest req){
		ErrorDetail err = new ErrorDetail(ex.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(OtpExpiredException.class)
	public ResponseEntity<ErrorDetail> otpExpiredExceptionHandeler(OtpExpiredException oer, WebRequest req){
		ErrorDetail err = new ErrorDetail(oer.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(EmailAlreadyAssociatedException.class)
	public ResponseEntity<ErrorDetail> emailAlreadyAssociatedExceptionHandeler(EmailAlreadyAssociatedException eaa, WebRequest req){
		ErrorDetail err = new ErrorDetail(eaa.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<ErrorDetail> bookNotFoundExceptionHandeler(BookNotFoundException e ,WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(),req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);			
	}
	@ExceptionHandler(ReviewNotFoundException.class)
	public ResponseEntity<ErrorDetail> reviewNotFoundExceptionHandeler(ReviewNotFoundException e ,WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorDetail> orderNotFoundExceptionHandelerEntity(OrderNotFoundException e , WebRequest req){
		ErrorDetail err = new  ErrorDetail(e.getMessage(),req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);
		
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetail> resourceNotFoundExceptionHandeler(ResourceNotFoundException e , WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(OrderProcessingException.class)
	public ResponseEntity<ErrorDetail> orderProcessingExceptionHandeler(OrderProcessingException e, WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(),req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.CONTINUE);
	}
	@ExceptionHandler(AuthorNotExistOrNotFoundException.class)
	public ResponseEntity<ErrorDetail> AuthorNotExistOrNotFoundException(AuthorNotExistOrNotFoundException e, WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(OrderDetailNotFoundOrNotExistException.class)
	public ResponseEntity<ErrorDetail> orderDetailNotFoundOrNotExistException(OrderDetailNotFoundOrNotExistException e , WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(),req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(WishlistNotFoundOrNotExistException.class)
	public ResponseEntity<ErrorDetail> wishlistNotFoundOrNotExistExceptionHandeler(WishlistNotFoundOrNotExistException e, WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<ErrorDetail> cartNotFoundExceptionHandeler(CartNotFoundException e, WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CartItemNotFoundException.class)
	public ResponseEntity<ErrorDetail> cartItemNotFoundExceptionHandler(CartItemNotFoundException e, WebRequest req){
		ErrorDetail err = new ErrorDetail(e.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err, HttpStatus.NOT_FOUND);
	}
}
