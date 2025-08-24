package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.CafeImage;
import com.likelion.hackathon.entity.CafeOperatingHours;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.entity.enums.SpaceType;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CafeResponseDto {
    private Long id;
    private String name;
    private String location;
    private String minOrder;
    private Long maxSeats;
    private SpaceType spaceType;
    private String customerPromotion;
    private List<CafeImageDto> images;
    private String operatingHours;
    private Long ownerUserId;
    private String phoneNumber;

    public CafeResponseDto(Cafe cafe) {
        this.id = cafe.getId();
        this.name = cafe.getName();
        this.location = cafe.getLocation();
        this.minOrder = cafe.getMinOrder();
        this.maxSeats = cafe.getMaxSeats();
        this.spaceType = cafe.getSpaceType();
        this.customerPromotion = cafe.getCustomerPromotion();

        this.images = cafe.getImages().stream()
                .map(img -> new CafeImageDto(img.getId(), img.getImageUrl()))
                .toList();

        this.operatingHours = getOperatingStatus(cafe.getOperatingHours());

        if (cafe.getBusinessInfo() != null && cafe.getBusinessInfo().getUser() != null) {
            User owner = cafe.getBusinessInfo().getUser();
            this.ownerUserId = owner.getId();
            this.phoneNumber = owner.getPhoneNumber();
        }
    }

    private String getOperatingStatus(CafeOperatingHours hours) {
        if (hours == null) {
            return "영업시간 등록 전 입니다";
        }

        LocalTime open = null;
        LocalTime close = null;
        Boolean isOpen = null;

        DayOfWeek today = LocalDate.now().getDayOfWeek();
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

        if (isOpen == null) {
            return "영업시간 등록 전 입니다";
        } else if (!isOpen) {
            return "휴무일";
        } else if (open != null && close != null) {
            return "영업중";
        } else {
            return "영업전";
        }
    }
}
