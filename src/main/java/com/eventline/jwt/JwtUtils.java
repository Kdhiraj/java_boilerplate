package com.eventline.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.application.name}")
    private String appName;

    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    @Value("${security.jwt.access-token-expiration}")
    private int jwtAccessTokenExpirationMs;

    @Value("${security.jwt.refresh-token-expiration}")
    private int jwtRefreshTokenExpirationMs;


    private SecretKey cachedSignInKey;


    /**
     * Retrieves the JWT token from the 'Authorization' header, if present.
     *
     * @param request HttpServletRequest object to extract the JWT token from headers.
     * @return the JWT token string without the "Bearer " prefix, or null if not found.
     */
    public String extractJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    /**
     * Lazy loads and caches the secret key used for signing and verifying JWT tokens.
     *
     * @return SecretKey for JWT signing/verification.
     */
    private SecretKey getSignInKey() {
        if (cachedSignInKey == null) {
            byte[] keyBytes = jwtSecret.getBytes();
            cachedSignInKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return cachedSignInKey;
    }

    /**
     * Generates a JWT token with the given subject and expiration time.
     *
     * @param subject      the subject for the JWT token (e.g., user ID).
     * @param expirationMs the expiration time in milliseconds.
     * @return the generated JWT token.
     */
    private String createJwtToken(String subject, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .issuer(appName)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }


    /**
     * Extracts all claims (payload data) from a JWT token.
     *
     * @param token the JWT token.
     * @return Claims object containing the token's payload data.
     * @throws JwtException if the token is invalid or expired.
     */
    private Claims extractClaims(String token) throws JwtException {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }


    /**
     * Extracts the user ID (subject) from a JWT token.
     *
     * @param token the JWT token.
     * @return the user ID (subject) stored in the token.
     */
    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }


    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token the JWT token.
     * @return true if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: {}", e.getMessage());
            return true;  // Token is expired
        }
    }

    /**
     * Validates the JWT token, checking for format, signature, and expiration.
     *
     * @param token the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isJwtValid(String token) {
        try {
            extractClaims(token);  // Will throw an exception if token is invalid or expired
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the provided JWT token is valid for the specified user.
     *
     * @param token       the JWT token to validate.
     * @param userDetails the user details to match against the token.
     * @return true if the token is valid for the user and not expired, false otherwise.
     */
    public boolean isTokenValidForUser(String token, UserDetails userDetails) {
        String tokenUserId = extractUserId(token);
        String userId = userDetails.getUsername();  // Assuming the user ID is the username
        return tokenUserId.equals(userId) && !isTokenExpired(token);
    }

    /**
     * Generates an access token with the default expiration time.
     *
     * @param subject the subject (e.g., user ID) for the JWT token.
     * @return the generated access token.
     */
    public String generateAccessToken(String subject) {
        return createJwtToken(subject, jwtAccessTokenExpirationMs);
    }

    /**
     * Generates a refresh token with the default expiration time.
     *
     * @param subject the subject (e.g., user ID) for the JWT token.
     * @return the generated refresh token.
     */
    public String generateRefreshToken(String subject) {
        return createJwtToken(subject, jwtRefreshTokenExpirationMs);
    }
}

