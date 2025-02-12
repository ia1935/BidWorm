package com.student.app.bidworm.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
//        System.out.println("Inside JwtAuthenticationFilter...");

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = extractToken(request);
//            System.out.println("Token extracted: " + token);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//                System.out.println("User details loaded: " + userDetails);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
//                System.out.println("Authentication set for: " + email);
            }
        }

        // Log filter continuation
//        System.out.println("Proceeding to next filter...");
        chain.doFilter(request, response);
    }




    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Check Authorization header
        if (bearerToken!= null && bearerToken.startsWith("Bearer ")) {
            //            System.out.println("Extracted Token from Header: " + token);
            return bearerToken.substring(7);
        }

        // Fallback: Check query parameter "token"
        String tokenFromQuery = request.getParameter("token");
        if (StringUtils.hasText(tokenFromQuery)) {
//            System.out.println("Extracted Token from Query Parameter: " + tokenFromQuery);
            return tokenFromQuery;
        }

        System.out.println("No token found in request");
        return null;
    }



}
