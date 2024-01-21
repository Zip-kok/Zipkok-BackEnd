package com.project.zipkok.service;

import com.project.zipkok.common.exception.oauth.NoExistUser;
import com.project.zipkok.dao.RedisDao;
import com.project.zipkok.dto.oauth.GetOauthLoginResponse;
import com.project.zipkok.dto.oauth.GetOauthResponse;
import com.project.zipkok.dto.oauth.GetOauthSignUpResponse;
import com.project.zipkok.model.User;
import com.project.zipkok.repository.UserRepository;
import com.project.zipkok.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.KAKAO_LOGIN_NEED_REGISTRATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisDao redisDao;

    public GetOauthResponse getOauthPage(String email){
        log.info("OauthService.getOauthPage");

        User user = this.userRepository.findByEmail(email);

        if(user == null){
            throw new NoExistUser(KAKAO_LOGIN_NEED_REGISTRATION, email);
        }

        long userId = user.getUserId();
        String accessToken = jwtProvider.createToken(email, userId);
        String refreshToken = jwtProvider.createRefreshToken(email);

        redisDao.setValues(email, refreshToken, Duration.ofMillis(jwtProvider.getJWT_REFRESH_EXPIRED_IN()));

        return new GetOauthLoginResponse(accessToken, refreshToken, jwtProvider.getJWT_EXPIRED_IN(), jwtProvider.getJWT_REFRESH_EXPIRED_IN());
    }
}
