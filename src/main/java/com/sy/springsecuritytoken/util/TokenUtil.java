package com.sy.springsecuritytoken.util;

import com.sy.springsecuritytoken.exception.AuthenticationProcessingException;
import com.sy.springsecuritytoken.response.ResponseCode;
import com.sy.springsecuritytoken.security.AccountInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;

public class TokenUtil {

    private static final String SECRET_KEY = "springSecuritySecretToken";

    public static String generateJwtToken(AccountInfo account) {
        final Date now = new Date();
        JwtBuilder builder = Jwts.builder()
            .setSubject(account.getEmail())
            .setClaims(createClaims(account))
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 15 * 60_000L))
            .signWith(SignatureAlgorithm.HS256, createSigningKey());
        return builder.compact();
    }

    public static void validToken(String accessToken) {
        if (accessToken.isBlank()) {
            throw new AuthenticationProcessingException(ResponseCode.NO_ACCESS_TOKEN);
        }
        if (!accessToken.startsWith("Bearer ")) {
            throw new AuthenticationProcessingException();
        }
        final String token = accessToken.substring(7);
        if (isTokenExpired(token)) {
            throw new AuthenticationProcessingException(ResponseCode.EXPIRED_TOKEN);
        }
    }

    public static AccountInfo parseToken(String accessToken) {
        validToken(accessToken);
        final Claims body = parseClaim(accessToken);
        final String email = body.get("email").toString();
        return AccountInfo.parse(email);
    }

    private static boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claim = parseClaim(token);
        return claimResolver.apply(claim);
    }

    private static Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Map<String, Object> createClaims(AccountInfo account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", account.getEmail());
        claims.put("role", account.getRole());
        return claims;
    }

    private static Claims parseClaim(String accessToken) {
        try {
            return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (JwtException e) {
            throw new AuthenticationProcessingException(ResponseCode.INVALID_AUTHENTICATION);
        }
    }
}
