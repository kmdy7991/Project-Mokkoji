package com.ssafy.mokkoji.member.oauth2.provider.info;


import com.ssafy.mokkoji.member.oauth2.provider.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2User extends OAuth2UserInfo {

    public GoogleOAuth2User(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
