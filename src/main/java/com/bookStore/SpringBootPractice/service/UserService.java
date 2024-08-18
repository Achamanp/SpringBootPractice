package com.bookStore.SpringBootPractice.service;

import java.security.Principal;
import org.springframework.security.authentication.BadCredentialsException;

import com.bookStore.SpringBootPractice.appConstant.UserPageResponse;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.payloads.UserDto;

public interface UserService {
	public UserDto findUserById(Integer id) throws UserNotFoundException;
	public UserDto findUser(String jwt) throws BadCredentialsException,UserNotFoundException;
	public UserDto updateUser(UserDto userDto,Principal principal,String password) throws UserNotFoundException;
	public UserPageResponse findUsers(Integer pageNumber, Integer pageSize,String sortDir,String sortBy);
	public void deleteUser(Integer id) throws UserNotFoundException;
	public UserDto changePassword(String oldPassword, String newPassword, Principal principal)
			throws UserNotFoundException;
	public String forgotPassword(String email) throws UserNotFoundException;
	public String changePassword(String newPassword,Integer otp);
	String changeEmailReq(String oldEmail, String savedPassword,String newEmail) throws UserNotFoundException;
	String changeEmail(Integer otp);

}
