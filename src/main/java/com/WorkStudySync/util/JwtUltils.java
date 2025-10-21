package com.WorkStudySync.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

@Component
public class JwtUltils {
    @Value("${key.token.jwt}")
    private String strKeyToken;

//    public String createToken(String data){
//        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKeyToken));
//        data = Jwts.builder().subject(data).signWith(secretKey).compact();
//        return data;
//    }
    public String createToken(Map<String, Object> claims) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKeyToken));

        return Jwts.builder()
                .claims(claims) // không dùng setClaims nữa
                .subject("Login JWT") // không dùng setSubject
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(secretKey) // không cần truyền SignatureAlgorithm nữa
                .compact();
    }


//    TODO: Giải mã token
    public String decryptToke(String token){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(strKeyToken));
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
