package com.project.zipkok.controller;

import com.project.zipkok.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import com.project.zipkok.common.response.BaseResponse;
import com.project.zipkok.dto.auth.PostRefreshTokenRequest;
import com.project.zipkok.dto.oauth.GetOauthResponse;
import com.project.zipkok.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.INVALID_REFRESHTOKEN;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("refreshToken")
    public BaseResponse<GetOauthResponse> reIssueToken(@Validated @ModelAttribute PostRefreshTokenRequest postRefreshTokenRequest, BindingResult bindingResult){
        log.info("AuthController.reIssueToken");

        if(bindingResult.hasErrors()){
            throw new JwtUnsupportedTokenException(INVALID_REFRESHTOKEN);
        }

        return new BaseResponse<>(this.authService.reIssue(postRefreshTokenRequest.getRefreshToken()));

    }
}
