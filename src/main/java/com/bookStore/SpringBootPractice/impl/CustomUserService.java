package com.bookStore.SpringBootPractice.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookStore.SpringBootPractice.entities.User;
import com.bookStore.SpringBootPractice.repositories.UserRepository;
@Service
public class CustomUserService implements UserDetailsService{
    private UserRepository userRepository;
    public CustomUserService(UserRepository userRepository) {
    	this.userRepository = userRepository;
    }
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = this.userRepository.findByUsername(username);
		return user.map(UserInfoDetail::new).orElseThrow(()-> new UsernameNotFoundException("User name not found"+ username));
	}

}
