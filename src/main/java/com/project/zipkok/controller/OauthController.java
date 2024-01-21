package com.project.zipkok.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.zipkok.common.response.BaseResponse;
import com.project.zipkok.dto.oauth.GetOauthResponse;
import com.project.zipkok.dto.oauth.kakao.OauthAccessTokenRequest;
import com.project.zipkok.dto.oauth.kakao.OauthAuthorizeCodeRequest;
import com.project.zipkok.dto.oauth.kakao.OauthUserInfoRequest;
import com.project.zipkok.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("oauth")
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("kakao/callback")
    public BaseResponse<GetOauthResponse> kakaoOauth(@ModelAttribute OauthAuthorizeCodeRequest oauthAuthorizeCodeRequest) throws JsonProcessingException {
        log.info("OauthController.kakakoOauth");

        //카카오 인증 서버로 access token 받아오기-------------------------------------------------------------------------
        RestTemplate rt = new RestTemplate(); //http request 만들기
        HttpHeaders headers = new HttpHeaders(); //http request header
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); //http request body
        params.add("grant_type", "authorization_code");
        params.add("client_id", "0f6d74e80d52bd95f1ea47704a80f915");
        params.add("redirect_uri", "http://localhost:9000/oauth/kakao/callback");
        params.add("code", oauthAuthorizeCodeRequest.getCode());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);  // header와 body 합치기

        ResponseEntity<String> response = rt.exchange(  //http request 보내기  
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //받아온 json을 object (OauthAccessTokenRequest) 에 mapping
        ObjectMapper objectMapper = new ObjectMapper();
        OauthAccessTokenRequest oauthAccessTokenRequest = null;

        try {
            oauthAccessTokenRequest = objectMapper.readValue(response.getBody(), OauthAccessTokenRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //카카오 api 서버에 정보 요청하기----------------------------------------------------------------------------------
        RestTemplate rt2 = new RestTemplate(); //http request 만들기
        HttpHeaders headers2 = new HttpHeaders(); //http request header
        headers2.add("Authorization", "Bearer " + oauthAccessTokenRequest.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoInfoRequest = new HttpEntity<>(headers2);  // header와 body 합치기

        ResponseEntity<String> response2 = rt2.exchange(  //http request 보내기
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoInfoRequest,
                String.class
        );

        //받아온 json을 object (OauthUserInfoRequest) 에 mapping
        ObjectMapper objectMapper2 = new ObjectMapper();
        OauthUserInfoRequest oauthUserInfoRequest = null;

        try {
            oauthUserInfoRequest = objectMapper2.readValue(response2.getBody(), OauthUserInfoRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new BaseResponse<>(oauthService.getOauthPage(oauthUserInfoRequest.getEmail()));
    }
}
