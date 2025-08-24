package com.likelion.hackathon.dto.CafeDto;

import lombok.*;

@Getter
@Setter
public class UnableTimeRequestDto {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}