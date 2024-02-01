package com.example.mokkoji.member.api.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_refresh_token")
public class UserRefreshToken {

    @JsonIgnore
    @Id
    @Column(name = "refresh_token_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenSeq;

    @Column(name = "user_id", length = 64, unique = true)
    @NotNull
    private String userId;

    @Column(name = "refresh_token", length = 256)
    @NotNull
    private String refreshToken;

//    @JsonIgnore
//    @Id
//    @Column(name = "REFRESH_TOKEN_SEQ")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long refreshTokenSeq;
//
//    @Column(name = "USER_ID", length = 64, unique = true)
//    @NotNull
//    @Size(max = 64)
//    private String userId;
//
//    @Column(name = "REFRESH_TOKEN", length = 256)
//    @NotNull
//    @Size(max = 256)
//    private String refreshToken;


    public UserRefreshToken(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

//    public UserRefreshToken(
//            @NotNull @Size(max = 64) String userId,
//            @NotNull @Size(max = 256) String refreshToken
//    ) {
//        this.userId = userId;
//        this.refreshToken = refreshToken;
//    }
}
