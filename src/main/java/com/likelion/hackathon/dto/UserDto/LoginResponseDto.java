package com.likelion.hackathon.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String message;
    private String accessToken;
    private String refreshToken;
    private Long userId;
}