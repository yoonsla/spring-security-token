package com.sy.springsecuritytoken.util;

import com.sy.springsecuritytoken.exception.AuthenticationProcessingException;
import com.sy.springsecuritytoken.user.domain.Account;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;

public class TokenUtil {

    private static final String SECRET_KEY = "springSecuritySecretToken";

    public static String generateJwtToken(Account account) {
        final Date now = new Date();
        JwtBuilder builder = Jwts.builder()
            .setSubject(account.getEmail())
            .setClaims(createClaims(account))
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 15 * 60_000L))
            .signWith(SignatureAlgorithm.HS256, createSigningKey());
        return builder.compact();
    }

    private static Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Map<String, Object> createClaims(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", account.getEmail());
        claims.put("role", account.getRole());
        return claims;
    }

    public static void validToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException e) {
            throw new AuthenticationProcessingException("토근 검증 실패");
        }
    }
}
