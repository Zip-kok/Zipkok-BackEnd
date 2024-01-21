package com.project.zipkok.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetOauthLoginResponse implements GetOauthResponse{
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private long refreshTokenExpiresIn;
}
