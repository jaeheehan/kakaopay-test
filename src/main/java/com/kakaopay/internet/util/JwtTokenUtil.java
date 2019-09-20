package com.kakaopay.internet.util;

import com.kakaopay.internet.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private String secret;

    private String refresh_key;

    @Autowired
    public JwtTokenUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.refresh_key}") String refresh_key){
        this.secret = secret;
        this.refresh_key = refresh_key;
    }

    public String getUsernameFromToken(boolean isAccess, String token) {
        return getClaimFromToken(isAccess, token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(boolean isAccess, String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(isAccess, token);
        return claimsResolver.apply(claims);
    }

    public Date getExpirationDateFromToken(boolean isAccess, String token) {
        return getClaimFromToken(isAccess, token, Claims::getExpiration);
    }

    private Claims getAllClaimsFromToken(boolean isAccess, String token) {
        return Jwts.parser().setSigningKey(isAccess ? secret : refresh_key).parseClaimsJws(token).getBody();
    }

    public String generateToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, member.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateRefreshToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRefreshToken(claims, member.getUsername());
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, refresh_key).compact();
    }

}