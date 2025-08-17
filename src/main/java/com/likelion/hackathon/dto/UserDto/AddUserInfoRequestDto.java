package com.likelion.hackathon.dto.UserDto;

import lombok.*;

@Getter
@Setter
public class AddUserInfoRequestDto {
    private Long userId;
    private String nickname;
    private String phonenumber;
}
