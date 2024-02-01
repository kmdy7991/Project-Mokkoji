package com.example.mokkoji.member.api.entity.user;

import com.example.mokkoji.member.oauth.entity.ProviderType;
import com.example.mokkoji.member.oauth.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor
@Entity
// @Builder
@Table(name = "user")
public class Users {
    @JsonIgnore
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int id;

    @Column(name = "user_id", length = 64, unique = true)
    @NotNull
    private String userId;

    @JsonIgnore
    @Column(name = "password", length = 128)
    @NotNull
    private String password;

    @Column(name = "user_nickname", length = 16, unique = true)
    @NotNull
    private String userNickname;

    @Column(name = "profile_image_url", length = 512)
    // @NotNull
    private String profileImageUrl;

    @Column(name = "role_type", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @Column(name = "provider_type", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType providerType;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;


    public Users(
            @NotNull String userId,
            @NotNull String userNickname,
            // @NotNull @Size(max = 512) String email,
            // @NotNull @Size(max = 1) String emailVerifiedYn,
            String profileImageUrl,
            @NotNull RoleType roleType,
            @NotNull ProviderType providerType,
            @NotNull LocalDateTime createdAt
    )
    {
        this.userId = userId;
        this.userNickname = userNickname;
        this.password = "NO_PASS";
        // this.email = email != null ? email : "NO_EMAIL";
        // this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;
        this.roleType = roleType;
        this.createdAt = createdAt;
        // this.modifiedAt = modifiedAt;
    }
}