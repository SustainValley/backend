package com.likelion.hackathon.dto.OAuthDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserDto {
    private String email;
    private String nickname;
    private String accessToken;
}