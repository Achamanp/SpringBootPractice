package com.bookStore.SpringBootPractice.Config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bookStore.SpringBootPractice.impl.CustomUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final CustomUserService customUserService;

    public JwtTokenFilter(JwtHelper jwtHelper, CustomUserService customUserService) {
        this.jwtHelper = jwtHelper;
        this.customUserService = customUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        try {
            if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
                token = jwtHeader.substring(7);
                username = jwtHelper.getUsernameFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserService.loadUserByUsername(username);

                    if (jwtHelper.validate(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a proper logging mechanism
        }

        filterChain.doFilter(request, response);
    }
}

