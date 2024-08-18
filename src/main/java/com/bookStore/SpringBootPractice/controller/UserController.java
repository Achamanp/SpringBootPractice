package com.bookStore.SpringBootPractice.controller;
import org.springframework.web.bind.annotation.RestController;

import com.bookStore.SpringBootPractice.Config.JwtHelper;
import com.bookStore.SpringBootPractice.appConstant.AuthRequest;
import com.bookStore.SpringBootPractice.appConstant.ForgotPassword;
import com.bookStore.SpringBootPractice.appConstant.SignupRequest;
import com.bookStore.SpringBootPractice.appConstant.changePassRequest;
import com.bookStore.SpringBootPractice.entities.OneTimePass;
import com.bookStore.SpringBootPractice.entities.Role;
import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.exceptions.EmailAlreadyAssociatedException;
import com.bookStore.SpringBootPractice.exceptions.InvalidOtpException;
import com.bookStore.SpringBootPractice.exceptions.UserNotFoundException;
import com.bookStore.SpringBootPractice.impl.UserServiceImpl;
import com.bookStore.SpringBootPractice.payloads.UserDto;
import com.bookStore.SpringBootPractice.repositories.OneTimePassRepository;
import com.bookStore.SpringBootPractice.repositories.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
//@RequestMapping("/api/user")
public class UserController {
	private UserServiceImpl userServiceImpl;
	private JwtHelper jwtHelper;
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private PasswordEncoder passwordEncoder;
	private JavaMailSender javaMailSender;
	private OneTimePassRepository oneTimePassRepository;
	public UserController(UserServiceImpl userServiceImpl,
			JwtHelper jwtHelper,UserRepository userRepository,ModelMapper modelMapper
			,PasswordEncoder passwordEncoder
			,AuthenticationManager authenticationManager
			,JavaMailSender javaMailSender
			,OneTimePassRepository oneTimePassRepository) {
		this.userServiceImpl = userServiceImpl;
		this.jwtHelper = jwtHelper;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.javaMailSender = javaMailSender;
		this.oneTimePassRepository = oneTimePassRepository;
	}
	@GetMapping("/findUser/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable Integer id) throws UserNotFoundException{
		UserDto userDto = this.userServiceImpl.findUserById(id);
		if(userDto == null) throw new UserNotFoundException("User not found with id " + id);
		return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
	}
	@GetMapping("/name/{username}")
	public ResponseEntity<UserDto> findUserByName(@PathVariable String username) throws BadCredentialsException, UserNotFoundException{
		String jwt = this.jwtHelper.generateToken(username);
		UserDto userDto = this.userServiceImpl.findUser(jwt);
		if(userDto==null) throw new UserNotFoundException("user not found !!");
		return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
		
	}

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody UserDto userDto, 
            Principal principal, 
            @RequestParam  String password) {
        try {
            UserDto updatedUser = userServiceImpl.updateUser(userDto, principal, password);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PostMapping("/signup-request")
    public ResponseEntity<Map<String, String>> registerUser(@RequestParam("email") String email) throws UserNotFoundException {
        if (this.userRepository.findByEmail(email) != null) {
            throw new EmailAlreadyAssociatedException("Email is used with another account: " + email);
        }
        Random rand = new Random();
        Integer otp = 100000 + rand.nextInt(900000);
        OneTimePass pass = new OneTimePass();
        pass.setOtp(otp);
        pass.setEmail(email);
        pass.setCreatedAt(LocalDateTime.now());
        pass.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        this.oneTimePassRepository.save(pass);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Complete Your Account Setup: Verify Your Email with OTP");
        message.setText("Hii there ,\r\n"
        		+ "\r\n"
        		+ "Thank you for choosing to create an account with Book Bazar! To complete your registration, please verify your email address by entering the One-Time Password (OTP) provided below.\r\n"
        		+ "\r\n"
        		+ "Your OTP: "+ otp +"\r\n"
        		+ "\r\n"
        		+ "This OTP is valid for the next 10 minutes. Please enter it on the registration page to confirm your email address and activate your account.\r\n"
        		+ "\r\n"
        		+ "If you did not request this account setup, please ignore this email.\r\n"
        		+ "\r\n"
        		+ "If you have any questions or need assistance, feel free to contact our support team at pathakachaman141@gmail.com.\r\n"
        		+ "\r\n"
        		+ "Thank you for joining us!\r\n"
        		+ "\r\n"
        		+ "Best regards,\r\n"
        		+ " Book Bazar Team");
        javaMailSender.send(message);
        Map<String,String> response = new HashMap<>();
        response.put("message", "OTP Sent Successfully");
        response.put("details", "An OTP has been sent to your email address. Please check your inbox and enter the OTP to complete your account setup.\r\n"
                + "If you don't receive the email within a few minutes, please check your spam/junk folder or request a new OTP.");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto,@RequestParam("Otp") Integer otp){
    	OneTimePass savedOtp = oneTimePassRepository.findByOtp(otp);
    	if(savedOtp==null) {
    		 throw new InvalidOtpException("Invalid OTP. You have X attempts remaining. Please check the code and try again. If you've run out of attempts, please request a new OTP.");
    	}
    	 if (savedOtp.getExpiredAt().isBefore(LocalDateTime.now())) {
 	        throw new InvalidOtpException("The OTP has expired. Please request a new OTP.");
 	    }
    	 User newUser = new User();
         newUser.setUsername(userDto.getUsername());
         newUser.setPassword(passwordEncoder.encode(userDto.getPassword())); 
         Set<Role> roles = new HashSet<>();
         roles.add(new Role("USER"));
         newUser.setEmail(savedOtp.getEmail());
         newUser.setRole(roles);
         newUser.setCreated_at(LocalDateTime.now());
         newUser.setUpdated_at(LocalDateTime.now());
         User savedUser = this.userRepository.save(newUser);
         UserDto savedUserDto = this.modelMapper.map(savedUser, UserDto.class);
         oneTimePassRepository.delete(savedOtp);
         return new ResponseEntity<UserDto>(savedUserDto,HttpStatus.CREATED);	
    }
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) throws UserNotFoundException {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            
            if (authenticate.isAuthenticated()) {
                System.out.println("Authentication successful for username: " + authRequest.getUsername());
                return jwtHelper.generateToken(authRequest.getUsername());
            } else {
                System.out.println("Authentication failed for username: " + authRequest.getUsername());
                throw new UserNotFoundException("Invalid username or password!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserNotFoundException("Error during authentication: " + e.getMessage());
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
    	
        try {
            userServiceImpl.forgotPassword(email);
            return ResponseEntity.ok("Password recovery OTP email sent successfully to " + email);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the OTP email.");        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("password") String newPassword,@RequestParam("otp") Integer otp) {
        try {
            String response = userServiceImpl.changePassword(newPassword, otp);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidOtpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/change-email-request")
    public ResponseEntity<String> changeEmailReq(
            @RequestParam("newEmail") String newEmail,
            @RequestParam("savedPassword") String savedPassword,
            @RequestParam("oldEmail") String oldEmail) {
        try {
            String responseMessage = userServiceImpl.changeEmailReq(newEmail, savedPassword, oldEmail);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK); 
        } catch ( BadCredentialsException e) {
            throw new BadCredentialsException("Enter valid email !!");
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestParam("otp") Integer otp) {
        try {
            String responseMessage = userServiceImpl.changeEmail(otp);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (InvalidOtpException e) {
           throw new InvalidOtpException("The otp you have entered is invalid please check the otp or request for new one");
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
