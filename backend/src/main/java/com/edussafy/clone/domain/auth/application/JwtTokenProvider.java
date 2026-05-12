package com.edussafy.clone.domain.auth.application;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final SecretKey secretKey;
    private final long accessTokenValidityMillis;
    private final long refreshTokenValidityMillis;

    @Autowired
    public JwtTokenProvider(
            @Value("${jwt.secret:1234567890123456789012345678901234567890123456789012345678901234}") String secret,
            @Value("${jwt.access-token-validity-millis:3600000}") long accessTokenValidityMillis
    ) {
        this(secret, accessTokenValidityMillis, 1209600000L);
    }

    public JwtTokenProvider(String secret, long accessTokenValidityMillis, long refreshTokenValidityMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityMillis = accessTokenValidityMillis;
        this.refreshTokenValidityMillis = refreshTokenValidityMillis;
    }

    public String createAccessToken(Long userId, String email, UserRole role) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + accessTokenValidityMillis);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + refreshTokenValidityMillis);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return REFRESH_TOKEN_TYPE.equals(claims.get("type", String.class));
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    public UserRole getRole(String token) {
        return UserRole.valueOf(parseClaims(token).get("role", String.class));
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
