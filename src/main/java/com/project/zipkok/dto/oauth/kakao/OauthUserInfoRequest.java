package com.project.zipkok.dto.oauth.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OauthUserInfoRequest {
    public long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Getter
    @Setter
    public static class Properties{
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }

    @Getter
    @Setter
    public static class KakaoAccount{
        public boolean profile_nickname_needs_agreement;
        public boolean profile_image_needs_agreement;
        public Profile profile;
        public boolean has_email;
        public boolean email_needs_agreement;
        public boolean is_email_valid;
        public boolean is_email_verified;
        public String email;
    }

    @Getter
    @Setter
    public static class Profile{
        public String nickname;
        public String thumbnail_image_url;
        public String profile_image_url;
        public boolean is_default_image;
    }

    public String getEmail(){
        return this.kakao_account.getEmail();
    }
}
