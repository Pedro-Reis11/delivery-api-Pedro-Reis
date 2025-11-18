package com.delivery_api.Projeto.Delivery.API.security;

import com.delivery_api.Projeto.Delivery.API.entity.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private Key signingKey;


    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.length() < 32) {
            throw new WeakKeyException(
                    "A chave JWT (jwt.secret) deve ter pelo menos 32 caracteres para HS256!"
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());

        if (expirationMs <= 0) {
            logger.warn("jwt.expiration não definido corretamente — usando 24h padrão.");
            expirationMs = 86400000; // 24h
        }
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Long extractRestaurantId(String token) {
        return extractClaim(token, claims -> claims.get("restaurantId", Long.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException e) {
            logger.warn("JWT expirado: {}", e.getMessage());
            throw e;
        }
        catch (JwtException e) {
            logger.error("Erro ao decodificar JWT: {}", e.getMessage());
            throw e;
        }
        catch (Exception e) {
            logger.error("Erro geral ao processar JWT: {}", e.getMessage());
            throw new JwtException("Falha ao processar o token JWT", e);
        }
    }


    public String generateToken(UserDetails userDetails, Usuario usuario) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId());
        claims.put("role", usuario.getRole().name());
        claims.put("restaurantId", usuario.getRestauranteId());

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);

            if (username == null) {
                logger.warn("JWT sem username (subject)");
                return false;
            }

            return username.equals(userDetails.getUsername()) && !isExpired(token);
        }
        catch (Exception e) {
            logger.error("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    private boolean isExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

}
