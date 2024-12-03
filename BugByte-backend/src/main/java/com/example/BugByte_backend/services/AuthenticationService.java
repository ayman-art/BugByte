package com.example.BugByte_backend.services;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    static Dotenv dotenv = Dotenv.configure().load();


    public static String generateJWT(long id, String username, Boolean admin){
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        String envKey = dotenv.get("JWT_Key");
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(envKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setId(String.valueOf(id))
                .setIssuedAt(today)
                .setSubject(username)
                .claim("is_admin", admin)
                .signWith(algorithm, signingKey)
                .setExpiration(new Date(now + 86400000)); // a day
        return builder.compact();
    }


    public static Claims parseToken(String jwt){

        String envKey = dotenv.get("JWT_Key");
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(envKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
    public static String refreshToken(String oldJwt) {
        try {
            Claims claims = parseToken(oldJwt);
            long id = Long.parseLong(claims.getId());
            String username = claims.getSubject();
            Object isAdmin = claims.get("is_admin");
            return generateJWT(id, username,(boolean) isAdmin);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to refresh token: " + e.getMessage());
        }
    }


}
