package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.MessageResponseDto;
import com.likelion.hackathon.dto.UserDto.*;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.repository.UserRepository;
import com.likelion.hackathon.security.jwt.TokenStatus;
import com.likelion.hackathon.service.UserService;
import com.likelion.hackathon.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public SignupResponseDto signup(
            @RequestParam String type,  // "per" 또는 "cor"
            @RequestBody SignupRequestDto request
    ) {
        // 잘못된 type이면 예외
        if (!type.equalsIgnoreCase("per") && !type.equalsIgnoreCase("cor")) {
            throw new IllegalArgumentException("허용되지 않은 회원 타입입니다. (pro, cor만 가능)");
        }

        // 사장님(cor)은 username을 businessnumber로 대체
        if (type.equalsIgnoreCase("cor")) {
            request.setUsername(request.getBusinessnumber());
        }

        Long savedUserId = userService.signup(type, request);
        return new SignupResponseDto("회원가입 성공", savedUserId);
    }

    @PostMapping("/signup/check-username")
    @Operation(summary = "아이디 중복 확인", description = "아이디 사용가능한지 이미 있는지 메시지 반환")
    public ResponseEntity<MessageResponseDto> checkUsername(@RequestBody UsernameCheckRequestDto requestDto) {
        MessageResponseDto response = userService.checkUsername(requestDto.getUsername());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/addinfo")
    @Operation(summary = "카카오 로그인 후 전화번호 추가 저장", description = "일반사용자 카카오 로그인 시 추가로 필요한 data 전화번호 저장")
    public ResponseEntity<MessageResponseDto> addPhoneNumber(
            @PathVariable Long userId,
            @RequestBody Map<String, String> requestBody) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String phoneNumber = requestBody.get("phonenumber");
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponseDto("전화번호가 저장되었습니다."));
    }


    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 성공시 토큰반환")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto("로그인 실패", null, null, null));
        }

        User user = optionalUser.get();
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDto(
                "로그인 성공", accessToken, refreshToken, user.getId()
        ));

    }
    @Operation(summary = "Refresh Access Token",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @RequestHeader("Authorization") String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        TokenStatus status = jwtUtil.validateRefreshToken(refreshToken);

        if (status == TokenStatus.EXPIRED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDto("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."));
        } else if (status == TokenStatus.INVALID) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDto("유효하지 않은 리프레시 토큰입니다."));
        }

        // 토큰에서 사용자 정보 추출
        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 새로운 AccessToken 발급
        String newAccessToken = jwtUtil.generateAccessToken(user);

        // 새로운 RefreshToken 발급 여부는 전략에 따라 다름
        return ResponseEntity.ok(new TokenResponseDto(newAccessToken, refreshToken));
    }

}
