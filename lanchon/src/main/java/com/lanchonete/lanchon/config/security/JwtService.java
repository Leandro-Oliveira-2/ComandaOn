package com.lanchonete.lanchon.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties props;

    public JwtService(JwtProperties props) {
        this.props = props;
    }

    private Key signingKey() {
        // Preferencial: secret em Base64. Se não for Base64 válido, cai para bytes do texto.
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.getSecret()));
        } catch (IllegalArgumentException e) {
            // Fallback (garanta >= 32 bytes)
            byte[] bytes = props.getSecret().getBytes(StandardCharsets.UTF_8);
            if (bytes.length < 32) {
                throw new IllegalArgumentException("JWT secret precisa ter pelo menos 32 bytes (256 bits).");
            }
            return Keys.hmacShaKeyFor(bytes);
        }
    }

    public String generateToken(String subject) {
        long now = System.currentTimeMillis();
        Date issued = new Date(now);
        Date exp = new Date(now + props.getExpiresInSeconds() * 1000);

        return Jwts.builder()
                .setIssuer(props.getIssuer())
                .setSubject(subject)
                .setIssuedAt(issued)
                .setExpiration(exp)
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        return parse(token).getBody().getSubject();
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(props.getIssuer())
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token);
    }
}
