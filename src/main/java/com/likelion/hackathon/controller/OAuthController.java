package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.OAuthDto.KakaoUserDto;
import com.likelion.hackathon.service.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoUserDto> kakaoCallback(@RequestParam String code) {
        KakaoUserDto kakaoUser = kakaoOAuthService.kakaoLogin(code);
        return ResponseEntity.ok(kakaoUser);
    }
}
