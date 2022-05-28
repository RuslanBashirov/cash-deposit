package org.bashirov.cashdeposit.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("#{T(java.lang.Integer).parseInt('${jwt.session.time}')}")
    private long sessionTime;

    public String generateToken(long userId, String role) {
        Date date = new Date(System.currentTimeMillis() + sessionTime);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder().addClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(date).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateToken(String token) throws ExpiredJwtException {
        try {
            if (token != null) {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
                return true;
            }
        } catch (Exception e) {
            log.error("Invalid Token");
        }
        return false;
    }

    public static String getTokenFromRequest(HttpServletRequest request) throws ExpiredJwtException {
        String token = request.getHeader("Authorization");
        if (hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public long getUserIdFromToken(String token) throws ExpiredJwtException {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.get("userId", Long.class);
    }
}