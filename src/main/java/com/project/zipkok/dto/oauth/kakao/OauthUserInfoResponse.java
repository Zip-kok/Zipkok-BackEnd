package com.project.zipkok.dto.oauth.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OauthUserInfoResponse {

    private String nickname;

    private String email;
}
