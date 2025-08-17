package com.likelion.hackathon.dto.CafeDto;

import com.likelion.hackathon.entity.CafeOperatingHours;
import lombok.Data;

import java.time.LocalTime;

@Data
public class CafeOperatingDto {
    private LocalTime monOpen;
    private LocalTime monClose;
    private Boolean monIsOpen;

    private LocalTime tueOpen;
    private LocalTime tueClose;
    private Boolean tueIsOpen;

    private LocalTime wedOpen;
    private LocalTime wedClose;
    private Boolean wedIsOpen;

    private LocalTime thuOpen;
    private LocalTime thuClose;
    private Boolean thuIsOpen;

    private LocalTime friOpen;
    private LocalTime friClose;
    private Boolean friIsOpen;

    private LocalTime satOpen;
    private LocalTime satClose;
    private Boolean satIsOpen;

    private LocalTime sunOpen;
    private LocalTime sunClose;
    private Boolean sunIsOpen;

    public static CafeOperatingDto fromEntity(CafeOperatingHours entity) {
        CafeOperatingDto dto = new CafeOperatingDto();
        dto.setMonOpen(entity.getMonOpen());
        dto.setMonClose(entity.getMonClose());
        dto.setMonIsOpen(entity.getMonIsOpen());

        dto.setTueOpen(entity.getTueOpen());
        dto.setTueClose(entity.getTueClose());
        dto.setTueIsOpen(entity.getTueIsOpen());

        dto.setWedOpen(entity.getWedOpen());
        dto.setWedClose(entity.getWedClose());
        dto.setWedIsOpen(entity.getWedIsOpen());

        dto.setThuOpen(entity.getThuOpen());
        dto.setThuClose(entity.getThuClose());
        dto.setThuIsOpen(entity.getThuIsOpen());

        dto.setFriOpen(entity.getFriOpen());
        dto.setFriClose(entity.getFriClose());
        dto.setFriIsOpen(entity.getFriIsOpen());

        dto.setSatOpen(entity.getSatOpen());
        dto.setSatClose(entity.getSatClose());
        dto.setSatIsOpen(entity.getSatIsOpen());

        dto.setSunOpen(entity.getSunOpen());
        dto.setSunClose(entity.getSunClose());
        dto.setSunIsOpen(entity.getSunIsOpen());

        return dto;
    }
}
