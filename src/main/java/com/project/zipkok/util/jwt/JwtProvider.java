package com.project.zipkok.util.jwt;

import io.jsonwebtoken.*;
import com.project.zipkok.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import com.project.zipkok.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import com.project.zipkok.common.exception.jwt.unauthorized.JwtMalformedTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
public class JwtProvider {

    @Value("${secret.jwt-secret-key}")
    private String JWT_SECRET_KEY;

    @Value("${secret.jwt-expired-in}")
    private long JWT_EXPIRED_IN;

    @Value("${secret.jwt-refresh-expired-in}")
    private long JWT_REFRESH_EXPIRED_IN;

    public String createToken(String email, long userId) {
        log.info("JWT key={}", JWT_SECRET_KEY);

        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + JWT_EXPIRED_IN);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("userId", userId) //payLoad에 이런식으로 값을 넣을 수 있다는 예시로, userId를 넣는 과정은 없어도 무방하다
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public String createRefreshToken(String email){
        log.info("JWT key={}", JWT_SECRET_KEY);

        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + JWT_REFRESH_EXPIRED_IN);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public boolean isExpiredToken(String token) throws JwtInvalidTokenException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET_KEY).build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            return true;

        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(MALFORMED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        } catch (JwtException e) {
            log.error("[JwtTokenProvider.validateAccessToken]", e);
            throw e;
        }
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}