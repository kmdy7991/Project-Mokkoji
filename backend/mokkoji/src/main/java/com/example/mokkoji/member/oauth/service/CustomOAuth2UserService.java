package com.example.mokkoji.member.oauth.service;

import com.example.mokkoji.member.api.entity.user.Users;
import com.example.mokkoji.member.api.repository.user.UserRepository;
import com.example.mokkoji.member.oauth.entity.ProviderType;
import com.example.mokkoji.member.oauth.entity.RoleType;
import com.example.mokkoji.member.oauth.entity.UserPrincipal;
import com.example.mokkoji.member.oauth.exception.OAuthProviderMissMatchException;
import com.example.mokkoji.member.oauth.info.OAuth2UserInfo;
import com.example.mokkoji.member.oauth.info.impl.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Users savedUsers = userRepository.findByUserId(userInfo.getId());

        if (savedUsers != null) {
            if (providerType != savedUsers.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUsers.getProviderType() + " account to login."
                );
            }
            updateUser(savedUsers, userInfo);
        } else {
            savedUsers = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUsers, user.getAttributes());
    }

    private Users createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        Users users = new Users(
//                userInfo.getId(),
//                userInfo.get(),
//                userInfo.getEmail(),
//                // "Y",
//                userInfo.getImageUrl(),
//                providerType,
//                RoleType.USER,
//                now,
//                now
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getImageUrl(),
                RoleType.USER,
                providerType,
                now
        );

        return userRepository.saveAndFlush(users);
    }

    private Users updateUser(Users users, OAuth2UserInfo userInfo) {
//        if (userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())) {
//            user.setUsername(userInfo.getName());
//        }
        if (userInfo.getName() != null && !users.getUserNickname().equals(userInfo.getName())) {
            users.setUserNickname(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && !users.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            users.setProfileImageUrl(userInfo.getImageUrl());
        }

        return users;
    }
}
