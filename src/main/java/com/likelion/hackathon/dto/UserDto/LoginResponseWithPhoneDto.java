package com.likelion.hackathon.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseWithPhoneDto {
    private LoginResponseDto loginResponse;
    private boolean hasPhoneNumber;
}