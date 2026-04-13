package org.example.hobbycatalog.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.hobbycatalog.DTO.AuthRequestDTO;
import org.example.hobbycatalog.entity.UsersInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

@Slf4j
@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String private_key;

    @Value("${app.jwt.expirationMs}")
    private Long access_token_valid_time;

    @Value("${app.jwt.refreshExpirationMs}")
    private Long refresh_token_valid_time;

    public String generateAccessJwtToken(UsersInfo user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getIdUser());
        claims.put("role", user.getRole() != null ? user.getRole().name() : "USER");

        log.info("generate Access Token");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + access_token_valid_time))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UsersInfo user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getIdUser());
        claims.put("type", "refresh");

        log.info("generate Refresh Token");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refresh_token_valid_time))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Универсальный метод извлечения claim'а из токена
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Валидация токена (подпись + срок действия)
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Извлечение всех claims из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Создание ключа из секретной строки (обязательно для HS256)
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(private_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
