package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.enums.SpaceType;
import lombok.Data;

@Data
public class CafeResponseDto {
    private Long id;
    private String name;
    private String location;
    private String minOrder;
    private Long maxSeats;
    private SpaceType spaceType;

    public CafeResponseDto(Cafe cafe) {
        this.id = cafe.getId();
        this.name = cafe.getName();
        this.location = cafe.getLocation();
        this.minOrder = cafe.getMinOrder();
        this.maxSeats = cafe.getMaxSeats();
        this.spaceType = cafe.getSpaceType();
    }
}
