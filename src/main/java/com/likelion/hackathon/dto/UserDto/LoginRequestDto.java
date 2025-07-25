package com.likelion.hackathon.dto.UserDto;

import lombok.*;

@Setter @Getter
public class LoginRequestDto {
    private String username;
    private String password;
}
