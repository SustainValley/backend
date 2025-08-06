package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.UserDto.LoginResponseDto;
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
    public ResponseEntity<LoginResponseDto> kakaoCallback(@RequestParam String code) {
        LoginResponseDto loginResponse = kakaoOAuthService.kakaoLogin(code);
        return ResponseEntity.ok(loginResponse);
    }
}
