package com.ssafy.mokkoji.member.users.entity;


import com.ssafy.mokkoji.member.oauth2.provider.OAuth2UserInfo;
import com.ssafy.mokkoji.member.users.enums.AuthProvider;
import com.ssafy.mokkoji.member.users.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
@AllArgsConstructor
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Entity
@Setter
public class Users implements OAuth2User, Serializable {  // 직렬화 추가, extends BaseDateEntity 제거

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        // 사용자 정보를 attributes에 추가
        attributes.put("id", this.oauth2Id);
        attributes.put("name", this.name);
        attributes.put("email", this.email);
        // ... 다른 필요한 속성 추가 ...

        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 정보를 반환 (Role 등)
        return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "refresh_token", length = 256)
    private String refreshToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Users update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();

        return this;
    }
}