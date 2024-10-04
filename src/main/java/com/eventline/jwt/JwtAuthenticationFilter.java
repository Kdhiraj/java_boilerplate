package com.eventline.jwt;

import com.eventline.api.v1.User.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final CustomUserDetailService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        logger.debug("Processing authentication filter for URI: {}", request.getRequestURI());     // Log incoming request URI for debugging
        try {

            String token = jwtUtils.extractJwtFromHeader(request); // Extract JWT token from the request header

            // If the token is present and valid, proceed to authenticate
            if (token != null && jwtUtils.isJwtValid(token)) {
                String userId = jwtUtils.extractUserId(token);
                UserDetails userDetails = userService.loadUserById(userId);
                if (jwtUtils.isTokenValidForUser(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    ); // Create an authentication token
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set additional authentication details
                    SecurityContextHolder.getContext().setAuthentication(authentication);  // Set the authentication in the SecurityContext

                    logger.info("Authenticated user: {} with roles: {}", userDetails.getUsername(), userDetails.getAuthorities());
                }
            }

        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
