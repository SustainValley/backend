package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.CafeOperatingHours;
import com.likelion.hackathon.entity.enums.SpaceType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CafeListDto {
    private Long cafeId;
    private String imageUrl;
    private String name;
    private String operatingHours;
    private SpaceType spaceType;
    private Long maxSeats;

    public static CafeListDto fromEntity(Cafe cafe) {
        LocalTime open = null;
        LocalTime close = null;
        Boolean isOpen = null;
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        CafeOperatingHours hours = cafe.getOperatingHours();

        if (hours != null) {
            switch (today) {
                case MONDAY -> {
                    open = hours.getMonOpen();
                    close = hours.getMonClose();
                    isOpen = hours.getMonIsOpen();
                }
                case TUESDAY -> {
                    open = hours.getTueOpen();
                    close = hours.getTueClose();
                    isOpen = hours.getTueIsOpen();
                }
                case WEDNESDAY -> {
                    open = hours.getWedOpen();
                    close = hours.getWedClose();
                    isOpen = hours.getWedIsOpen();
                }
                case THURSDAY -> {
                    open = hours.getThuOpen();
                    close = hours.getThuClose();
                    isOpen = hours.getThuIsOpen();
                }
                case FRIDAY -> {
                    open = hours.getFriOpen();
                    close = hours.getFriClose();
                    isOpen = hours.getFriIsOpen();
                }
                case SATURDAY -> {
                    open = hours.getSatOpen();
                    close = hours.getSatClose();
                    isOpen = hours.getSatIsOpen();
                }
                case SUNDAY -> {
                    open = hours.getSunOpen();
                    close = hours.getSunClose();
                    isOpen = hours.getSunIsOpen();
                }
            }
        }

        String operatingHoursStr;
        if (isOpen == null) {
            operatingHoursStr = "영업시간 등록 전 입니다";
        } else if (!isOpen) {
            operatingHoursStr = "휴무일";
        } else if (open != null && close != null) {
            operatingHoursStr = open + " - " + close;
        } else {
            operatingHoursStr = "영업시간 등록 전 입니다";
        }

        return new CafeListDto(
                cafe.getId(),
                cafe.getImageUrl(),
                cafe.getName(),
                operatingHoursStr,
                cafe.getSpaceType(),
                cafe.getMaxSeats()
        );
    }
}
