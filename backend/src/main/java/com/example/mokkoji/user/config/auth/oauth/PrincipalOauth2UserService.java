package com.example.mokkoji.user.config.auth.oauth;

import com.example.mokkoji.user.config.auth.PrincipalDetails;
import com.example.mokkoji.user.config.auth.oauth.provider.GoogleUserInfo;
import com.example.mokkoji.user.config.auth.oauth.provider.NaverUserInfo;
import com.example.mokkoji.user.config.auth.oauth.provider.OAuth2UserInfo;
import com.example.mokkoji.user.model.User;
import com.example.mokkoji.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        System.out.println("userRequest: " + userRequest.getClientRegistration());
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());

        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로긴 완료 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필을 받아줌
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("getAttributes: " + oauth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        }
        else{
            System.out.println("ㅠㅠ");
        }
        // 회원가입
        // sub = google providerId
        String provider = oAuth2UserInfo.getProvider();
        System.out.println("provider: " + provider);
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;  // google_sub
        String email = oauth2User.getAttribute("email");
//        String password = bCryptPasswordEncoder.encode("겟인데어");
        String role = "USER";

        // 회원가입이 이미 되어있는지 체크
        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            System.out.println("최초 로긴");
            userEntity = User.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .password("123")
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        else{
            System.out.println("이미 로긴 했음");
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());  // Oauth 유저 타입 리턴
    }

}
