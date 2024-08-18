package com.bookStore.SpringBootPractice.impl;

import java.util.stream.Collectors;
import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.bookStore.SpringBootPractice.entities.User;

public class UserInfoDetail implements UserDetails {
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoDetail(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getRole().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

  
}
