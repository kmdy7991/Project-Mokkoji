package com.ssafy.mokkoji.member.oauth2.provider;



import com.ssafy.mokkoji.member.oauth2.provider.info.GoogleOAuth2User;
import com.ssafy.mokkoji.member.oauth2.provider.info.KakaoOAuth2User;
import com.ssafy.mokkoji.member.oauth2.provider.info.NaverOAuth2User;
import com.ssafy.mokkoji.member.users.enums.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        return switch (authProvider) {
            case GOOGLE -> new GoogleOAuth2User(attributes);
            case NAVER -> new NaverOAuth2User(attributes);
            case KAKAO -> new KakaoOAuth2User(attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
}
