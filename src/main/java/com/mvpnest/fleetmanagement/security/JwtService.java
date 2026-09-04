package com.mvpnest.fleetmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // 24 hours
    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24;
    private final SecretKey signingKey;

    public JwtService(@Value("${JWT_SECRET}") String secretKey) {
        try {
            this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        } catch (Exception e) {
            throw new IllegalStateException("JWT_SECRET must be a valid Base64-encoded secret", e);
        }
    }

    // ================== GENERATE TOKEN ==================

    public String generateToken(String email) {
        return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(signingKey, SignatureAlgorithm.HS256).compact();
    }

    // ================== EXTRACT EMAIL ==================

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ================== VALIDATE TOKEN ==================

    public boolean isTokenValid(String token, String email) {
        try {
            String extractedEmail = extractEmail(token);

            return extractedEmail != null && extractedEmail.equals(email) && !isTokenExpired(token);

        } catch (Exception e) {
            return false;
        }
    }

    // ================== TOKEN EXPIRATION ==================

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ================== CLAIMS ==================

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }
}