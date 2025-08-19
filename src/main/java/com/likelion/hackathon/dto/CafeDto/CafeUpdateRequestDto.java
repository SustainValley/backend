package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.enums.SpaceType;
import lombok.*;

@Data
public class CafeUpdateRequestDto {

    private String minOrder;
    private Integer maxCapacity;
    private SpaceType spaceType;
}
