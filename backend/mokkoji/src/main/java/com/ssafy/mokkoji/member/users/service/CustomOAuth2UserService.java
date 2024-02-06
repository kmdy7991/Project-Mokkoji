package com.ssafy.mokkoji.member.users.service;


import com.ssafy.mokkoji.member.oauth2.provider.OAuth2UserInfo;
import com.ssafy.mokkoji.member.oauth2.provider.OAuth2UserInfoFactory;
import com.ssafy.mokkoji.member.users.entity.Users;
import com.ssafy.mokkoji.member.users.enums.AuthProvider;
import com.ssafy.mokkoji.member.users.enums.Role;
import com.ssafy.mokkoji.member.users.repository.UserRegistrationRequest;
import com.ssafy.mokkoji.member.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {  // OAuth2UserService<OAuth2UserRequest, OAuth2User>

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;

    // loadUser 메서드가 호출되었을 때 OAuth2UserRequest 객체에는 oauth 인증 결과인 access token을 포함
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("loadUser");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2UserServiceError"), ex);
        }
    }

    //  google, kakao, naver 등, oauth 인증 요청 플랫폼을 구분해
    //  각각의 사용자 정보 형태에 맞는 OAuth2UserInfo 객체를 가져와 회원가입 또는 회원 정보 갱신 로직을 처리
    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        System.out.println("processoAuth2User");
        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        // log.info("CustomOauth Line 116 = {}",authProvider);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());
        // log.info("CustomOauth Line 119 = {}",oAuth2UserInfo);


        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        return (OAuth2User) userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .map(user -> updateUser(user, oAuth2UserInfo))
                .orElseGet(() -> registerUser(authProvider, oAuth2UserInfo));
    }

    private Users registerUser(AuthProvider provider, OAuth2UserInfo oAuth2UserInfo) {
        System.out.println("가입합니다");

        Users user = Users.builder()
                .provider(provider)
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .oauth2Id(oAuth2UserInfo.getOAuth2Id())
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    private Users updateUser(Users user, OAuth2UserInfo oAuth2UserInfo) {

        return userRepository.save(user.update(oAuth2UserInfo));
    }

    // 닉네임 중복체크
    public boolean checkNickname(String nickname) {
        log.info("닉네임 중복체크");
        return userRepository.existsByUserNickname(nickname);
    }


    // 닉네임 추가
    public void insertNickname(UserRegistrationRequest registrationRequest) {
        log.info("회원가입");
        // User 엔터티 생성 및 필요한 정보 설정

        Users user = new Users();
        user.setEmail(registrationRequest.getEmail());
        user.setUserNickname(registrationRequest.getNickname());
        // 다른 필요한 정보들 설정

        // UserRepository를 통해 저장
        userRepository.save(user);
    }
}
