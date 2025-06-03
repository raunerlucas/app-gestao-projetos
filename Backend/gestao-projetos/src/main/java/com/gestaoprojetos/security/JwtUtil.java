package com.gestaoprojetos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Classe utilitária para criar e validar JWTs.
 */
@Component
public class JwtUtil {

    /**
     * Chave secreta usada para assinar o token.
     * Em produção, use valor armazenado em variável de ambiente ou vault.
     */
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Expiração do token: 2 horas (em milissegundos)
     */
    private final long jwtExpirationMs = 1000 * 60 * 60 * 2;

    /**
     * Gera um token JWT baseado no username.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Extrai o username (sub) do token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Checa se o token expirou.
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /**
     * Valida o token (assinatura e validade).
     */
    public boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Recupera qualquer claim usando o Claims resolver.
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}