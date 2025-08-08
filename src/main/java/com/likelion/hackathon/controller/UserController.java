package com.likelion.hackathon.controller;

import com.likelion.hackathon.dto.UserDto.LoginRequestDto;
import com.likelion.hackathon.dto.UserDto.LoginResponseDto;
import com.likelion.hackathon.dto.UserDto.SignupResponseDto;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.repository.UserRepository;
import com.likelion.hackathon.service.UserService;
import com.likelion.hackathon.security.jwt.JwtUtil;
import com.likelion.hackathon.dto.UserDto.SignupRequestDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Autowired
    private UserService userService;
    private JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
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

}
