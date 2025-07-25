package com.likelion.hackathon.dto.UserDto;

import lombok.*;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String provider;
}
