package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.enums.SpaceType;
import lombok.*;

@Data
public class CafeUpdateRequestDto {

    private String minOrder; // 최소 주문
    private Integer maxCapacity; // 한 예약 당 최대 수용인원
    private String content; // 공간 설명 (직접 입력)

    private SpaceType spaceType; // 선택지 ENUM
}
