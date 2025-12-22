package com.likelion.hackathon.dto.UserDto;

import lombok.*;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String nickname;
    private String password;
    private String phoneNumber;
    private String provider;

    // 사장님(cor) 회원가입 시 추가 필드
    private String businessnumber;
    private String presidentname;
    private String businessname;
    private String zipcode;
    private String address;
}
