package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.UserDto.LoginResponseDto;
import com.likelion.hackathon.dto.UserDto.LoginResponseWithPhoneDto;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.repository.UserRepository;
import com.likelion.hackathon.service.KakaoOAuthService;
import com.likelion.hackathon.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserRepository userRepository;

    @GetMapping("/kakao/callback/2")
    @Operation(summary = "카카오 로그인", description = "기본 카카오 로그인")
    public ResponseEntity<LoginResponseDto> kakaoCallback(@RequestParam String code) {
        LoginResponseDto loginResponse = kakaoOAuthService.kakaoLogin(code);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 로그인 (전화번호 여부 포함)", description = "카카오 로그인 후 전화번호 등록 여부를 함께 반환합니다.")
    public ResponseEntity<LoginResponseWithPhoneDto> kakaoCallbackWithPhone(@RequestParam String code) {
        LoginResponseDto loginResponse = kakaoOAuthService.kakaoLogin(code);

        Optional<User> optionalUser = userRepository.findById(loginResponse.getUserId());
        if (optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        User user = optionalUser.get();
        boolean hasPhoneNumber = user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty();

        LoginResponseWithPhoneDto response = new LoginResponseWithPhoneDto(loginResponse, hasPhoneNumber);
        return ResponseEntity.ok(response);
    }

}
