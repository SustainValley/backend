package com.likelion.hackathon.dto.CafeDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CafeImageResponseDto {
    private String message;
    private Long imageId;
}