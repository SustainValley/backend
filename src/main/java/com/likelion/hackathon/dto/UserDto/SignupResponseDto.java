package com.likelion.hackathon.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponseDto {
    private String message;
    private Long userId;
    private String address;
}
