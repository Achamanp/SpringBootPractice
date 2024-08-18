package com.bookStore.SpringBootPractice.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bookStore.SpringBootPractice.payloads.UserDto;
import com.bookStore.SpringBootPractice.Config.JwtHelper;
import com.bookStore.SpringBootPractice.appConstant.UserPageResponse;
import com.bookStore.SpringBootPractice.entities.Otp;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.exceptions.InvalidOtpException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.repositories.OtpRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
import com.bookStore.SpringBootPractice.service.UserService;


@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private JwtHelper jwtHelper;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender javaMailSender;
    private OtpRepository otpRepository;
    public UserServiceImpl(UserRepository userRepository,ModelMapper modelmapper,PasswordEncoder passwordEncoder,JavaMailSender javaMailSender,OtpRepository otpRepository) {
    	this.userRepository = userRepository;
    	this.modelMapper = modelmapper;
    	this.passwordEncoder = passwordEncoder;
    	this.javaMailSender = javaMailSender;
    	this.otpRepository = otpRepository;
    }
	@Override
	public UserDto findUserById(Integer id) throws UserNotFoundException {
		Optional<User> opt = this.userRepository.findById(id);
		if(opt!=null) opt.get();
		else throw new UserNotFoundException("User not found with id : " + id);
		return this.modelMapper.map(opt, UserDto.class);
	}

	@Override
	public UserDto findUser(String jwt) throws BadCredentialsException, UserNotFoundException {
		String username = jwtHelper.getUsernameFromToken(jwt);
		if(username == null) {
			throw new BadCredentialsException("recieved invalid token------");
		}
		Optional <User> user = this.userRepository.findByUsername(username);
		if(user == null) {
			throw new UserNotFoundException("User not found with username " + username); 
		}
		
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Principal principal,String password) throws UserNotFoundException {
		String username = principal.getName();
		Optional<User> userOpt = this.userRepository.findByUsername(username);
		if(userOpt.isEmpty()) {
			 throw new UserNotFoundException("User not found with username: " + username);
		}
		User user = userOpt.get();
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect");
		}
		user.setUsername(userDto.getUsername());
		user.setUpdated_at(LocalDateTime.now());
		User updatedUser = this.userRepository.save(user);
		return this.modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public UserPageResponse findUsers(Integer pageNumber, Integer pageSize, String sortDir, String sortBy) {
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}
		else {
			sort = Sort.by(sortBy).descending();
		}
		Pageable p = PageRequest.of(pageNumber,pageSize, sort);
		Page<User> pages = this.userRepository.findAll(p);
		List<User> users = pages.getContent();
		List<UserDto> dtos = users.stream().map(userDto-> 
		this.modelMapper.map(userDto, UserDto.class)).collect(Collectors.toList());
		UserPageResponse response = new UserPageResponse();
		response.setContent(dtos);
		response.setLastpage(pages.isLast());
		response.setPageNumber(pages.getNumber());
		response.setPageSize(pages.getSize());
		response.setTotalElement(pages.getTotalElements());
		response.setTotalPages(pages.getTotalPages());
		return response;
	}

	@Override
	public void deleteUser(Integer id) throws UserNotFoundException {
		Optional<User> opt = this.userRepository.findById(id);
		if(opt!=null) {
			userRepository.deleteById(id);
		}
		else {
			throw new UserNotFoundException("User not found with this id " + id);
		}		
	}
	@Override
	public UserDto changePassword(String oldPassword, String newPassword, Principal principal) throws UserNotFoundException {
		String userName = principal.getName();
       Optional<User> currUser = this.userRepository.findByUsername(userName);
       if (!currUser.isPresent()) {
           throw new UserNotFoundException("User not found with username: " + userName);
       }
       User user = currUser.get();
       if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
           throw new IllegalArgumentException("Old password is incorrect");
       }
       user.setPassword(passwordEncoder.encode(newPassword));
       User updatedUser = userRepository.save(user);
       UserDto userDto = this.modelMapper.map(updatedUser, UserDto.class);
		return userDto;
	}
	public Integer generateOtp() {
		  Random random = new Random();
		    int otp = 100000 + random.nextInt(900000);
        return otp;
	}
	@Override
	public String forgotPassword(String email) throws UserNotFoundException {
	    User user = userRepository.findByEmail(email);
	    if (user == null) {
	        throw new UserNotFoundException("User not found with email !! " + email);
	    } else {
	        Random rand = new Random();
	        int recievedOtp = 100000 + rand.nextInt(900000);
	        Otp otp = new Otp();
	        otp.setOtp(recievedOtp);
	        otp.setCreatedAt(LocalDateTime.now());
	        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
	        otp.setUser(user);
	        otpRepository.save(otp);
	        
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(user.getEmail()); 
	        message.setSubject("Password Recovery OTP");
	        message.setText("Dear " + user.getUsername() + ",\r\n"
	                + "\r\n"
	                + "To reset your password, please use the following One-Time Password (OTP):\r\n"
	                + "\r\n"
	                + recievedOtp + "\r\n"
	                + "\r\n"
	                + "This OTP is valid for the next 10 minutes. Please do not share this code with anyone. "
	                + "If you did not request a password reset, please ignore this email.\r\n"
	                + "\r\n"
	                + "Thank you,\r\n"
	                + "BookBazaar Support Team");
	        javaMailSender.send(message);
	        return "Password recovery OTP email sent successfully to " + email;
	    }
	}
	@Override
	public String changePassword(String newPassword, Integer otp) {
	    Otp savedOtp = otpRepository.findByOtp(otp);
	    if (savedOtp == null) {
	        throw new InvalidOtpException("Invalid OTP. You have X attempts remaining. Please check the code and try again. If you've run out of attempts, please request a new OTP.");
	    }
	    if (savedOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
	    	otpRepository.delete(savedOtp);
	        throw new InvalidOtpException("The OTP has expired. Please request a new OTP.");
	    }
	    User user = savedOtp.getUser();
	    if (passwordEncoder.matches(newPassword, user.getPassword())) {
	        return "Your new password cannot be the same as your previous password. Please choose a different password.";
	    }
	    user.setPassword(passwordEncoder.encode(newPassword));
	    userRepository.save(user);
	    otpRepository.delete(savedOtp);
	    return "Your password has been successfully changed.";
	}
	@Override
	public String changeEmailReq(String newEmail,String savedPassword,String oldEmail) throws UserNotFoundException {
		User user = this.userRepository.findByEmail(oldEmail);
		
		if(user==null) {
			throw new  UserNotFoundException("Email not found with this email " + oldEmail);
		}
		if(!passwordEncoder.matches(savedPassword, user.getPassword())) {
			throw new BadCredentialsException("Wrong password !! Please check your password and try again." );
		}
		Random rand = new Random();
		Integer otp = 100000 + rand.nextInt(900000);
		Otp newOtp = new Otp();
		newOtp.setOtp(otp);
		newOtp.setUser(user);
		newOtp.setCreatedAt(LocalDateTime.now());
		newOtp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
		newOtp.setEmail(newEmail);
		otpRepository.save(newOtp);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(newEmail);
		message.setSubject("Verify Your Email Address for Email Change");
		message.setText("Hi there,\r\n"
	            + "\r\n"
	            + "You have requested to change your email address. Please verify your request by entering the One-Time Password (OTP) provided below.\r\n"
	            + "\r\n"
	            + "Your OTP: " + otp + "\r\n"
	            + "\r\n"
	            + "This OTP is valid for the next 10 minutes. Please enter it on the registration page to confirm the email change.\r\n"
	            + "\r\n"
	            + "If you did not request this change, please ignore this email.\r\n"
	            + "\r\n"
	            + "Best regards,\r\n"
	            + "Book Bazar");
		javaMailSender.send(message);
		 return "OTP sent successfully. Please check your email to verify the email change request.";	
	}
	@Override
	public String changeEmail(Integer otp) {
		Otp savedOtp = this.otpRepository.findByOtp(otp);
		if(savedOtp==null) {
			throw new InvalidOtpException("The Otp you have entered is invalid please check the otp and try again or request for a new one ");
		}
		if(savedOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
			otpRepository.delete(savedOtp);
			 throw new InvalidOtpException("The OTP has expired. Please request a new OTP.");	 
		}
		User user = savedOtp.getUser();
		user.setEmail(savedOtp.getEmail());
		userRepository.save(user);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(savedOtp.getEmail());
		message.setSubject("Your Email Address Has Been Successfully Updated");
		message.setText("Hi there,\r\n"
				+ "\r\n"
				+ "We are pleased to inform you that your email address has been successfully updated.\r\n"
				+ "Thank you for being a part of Book Bazar!\r\n"
				+ "\r\n"
				+ "Best regards,\r\n"
				+ "The Book Bazar Team");
		return "Your email has been changed successfully";	
	}
}
