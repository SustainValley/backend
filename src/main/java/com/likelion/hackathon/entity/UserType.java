package com.likelion.hackathon.entity;

public enum UserType {
    PER, COR;

    public static UserType fromString(String value) {
        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("허용되지 않은 회원 타입입니다. (per, cor만 가능)");
        }
    }
}