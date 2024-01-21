package com.project.zipkok.service;

import com.project.zipkok.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import com.project.zipkok.dao.RedisDao;
import com.project.zipkok.dto.oauth.GetOauthLoginResponse;
import com.project.zipkok.dto.oauth.GetOauthResponse;
import com.project.zipkok.repository.UserRepository;
import com.project.zipkok.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.INVALID_REFRESHTOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final RedisDao redisDao;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public GetOauthResponse reIssue(String refreshToken){
        log.info("AuthController.reIssue");

        this.jwtProvider.isExpiredToken(refreshToken);
        String email = this.jwtProvider.getEmail(refreshToken);

        if(!refreshToken.equals(this.redisDao.getValues(email))){
            throw new JwtUnsupportedTokenException(INVALID_REFRESHTOKEN);
        }

        long userId = this.userRepository.findByEmail(email).getUserId();

        String accessToken = this.jwtProvider.createToken(email, userId);
        String newRefreshToken = this.jwtProvider.createRefreshToken(email);

        redisDao.setValues(email, newRefreshToken, Duration.ofMillis(jwtProvider.getJWT_REFRESH_EXPIRED_IN()));

        return new GetOauthLoginResponse(accessToken, newRefreshToken, jwtProvider.getJWT_EXPIRED_IN(), jwtProvider.getJWT_REFRESH_EXPIRED_IN());
    }
}
