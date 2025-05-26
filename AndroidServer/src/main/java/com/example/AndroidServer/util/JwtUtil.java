package com.example.AndroidServer.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 使用安全的固定密钥配置方式
    @Value("${jwt.secret}") // 从配置文件注入密钥
    private String secretBase64; // 使用 base64 编码的密钥

    private Key SECRET_KEY; // 最终使用的密钥对象
    private final long EXPIRATION = 86400000; // 24小时

    // 初始化密钥配置
    @PostConstruct
    public void init() {
        SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
