package com.likelion.hackathon.service;

import com.likelion.hackathon.dto.CafeDto.CafeAbleTimeRequestDto;
import com.likelion.hackathon.dto.CafeDto.CafeOperatingDto;
import com.likelion.hackathon.dto.CafeDto.UnableTimeRequestDto;
import com.likelion.hackathon.dto.CafeDto.UnableTimeResponseDto;
import com.likelion.hackathon.entity.Cafe;
import com.likelion.hackathon.entity.enums.DayOfWeek;
import com.likelion.hackathon.entity.CafeOperatingHours;
import com.likelion.hackathon.entity.CafeUnavailableBlock;
import com.likelion.hackathon.repository.CafeOperatingHoursRepository;
import com.likelion.hackathon.repository.CafeRepository;
import com.likelion.hackathon.repository.CafeUnavailableBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeOperatingService {

    private final CafeRepository cafeRepository;
    private final CafeOperatingHoursRepository cafeOperatingHoursRepository;
    private final CafeUnavailableBlockRepository unavailableRepo;

    @Transactional(readOnly = true)
    public CafeOperatingHours getOperatingHours(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        return cafeOperatingHoursRepository.findByCafe(cafe)
                .orElseThrow(() -> new IllegalArgumentException("운영시간 정보가 없습니다."));
    }

    @Transactional
    public CafeOperatingHours updateOperatingHours(Long cafeId, CafeOperatingDto request) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        CafeOperatingHours hours = cafeOperatingHoursRepository.findByCafe(cafe)
                .orElseGet(() -> {
                    CafeOperatingHours newHours = new CafeOperatingHours();
                    newHours.setCafe(cafe);
                    return newHours;
                });

        hours.setMonOpen(request.getMonOpen());
        hours.setMonClose(request.getMonClose());
        hours.setMonIsOpen(request.getMonIsOpen());

        hours.setTueOpen(request.getTueOpen());
        hours.setTueClose(request.getTueClose());
        hours.setTueIsOpen(request.getTueIsOpen());

        hours.setWedOpen(request.getWedOpen());
        hours.setWedClose(request.getWedClose());
        hours.setWedIsOpen(request.getWedIsOpen());

        hours.setThuOpen(request.getThuOpen());
        hours.setThuClose(request.getThuClose());
        hours.setThuIsOpen(request.getThuIsOpen());

        hours.setFriOpen(request.getFriOpen());
        hours.setFriClose(request.getFriClose());
        hours.setFriIsOpen(request.getFriIsOpen());

        hours.setSatOpen(request.getSatOpen());
        hours.setSatClose(request.getSatClose());
        hours.setSatIsOpen(request.getSatIsOpen());

        hours.setSunOpen(request.getSunOpen());
        hours.setSunClose(request.getSunClose());
        hours.setSunIsOpen(request.getSunIsOpen());

        return cafeOperatingHoursRepository.save(hours);
    }

    // 카페 예약 불가능 시간 설정
    public void addUnavailableTime(Long cafeId, UnableTimeRequestDto dto) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        com.likelion.hackathon.entity.enums.DayOfWeek dayOfWeek =
                com.likelion.hackathon.entity.enums.DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase());

        CafeUnavailableBlock block = new CafeUnavailableBlock();
        block.setCafe(cafe);
        block.setDayOfWeek(dayOfWeek);
        block.setStartTime(LocalTime.parse(dto.getStartTime()));
        block.setEndTime(LocalTime.parse(dto.getEndTime()));

        unavailableRepo.save(block);
    }

    public UnableTimeResponseDto getUnavailableBlocks(Long cafeId,
                                                      com.likelion.hackathon.entity.enums.DayOfWeek dayOfWeek) {

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        // 1. 카페 운영시간 조회
        CafeOperatingHours operatingHours = cafeOperatingHoursRepository.findByCafe(cafe)
                .orElseThrow(() -> new IllegalArgumentException("운영 시간이 등록되지 않은 카페입니다."));

        UnableTimeResponseDto dto = new UnableTimeResponseDto();

        switch (dayOfWeek) {
            case MON -> {
                dto.setOpen(operatingHours.getMonOpen().toString());
                dto.setClose(operatingHours.getMonClose().toString());
            }
            case TUE -> {
                dto.setOpen(operatingHours.getTueOpen().toString());
                dto.setClose(operatingHours.getTueClose().toString());
            }
            case WED -> {
                dto.setOpen(operatingHours.getWedOpen().toString());
                dto.setClose(operatingHours.getWedClose().toString());
            }
            case THU -> {
                dto.setOpen(operatingHours.getThuOpen().toString());
                dto.setClose(operatingHours.getThuClose().toString());
            }
            case FRI -> {
                dto.setOpen(operatingHours.getFriOpen().toString());
                dto.setClose(operatingHours.getFriClose().toString());
            }
            case SAT -> {
                dto.setOpen(operatingHours.getSatOpen().toString());
                dto.setClose(operatingHours.getSatClose().toString());
            }
            case SUN -> {
                dto.setOpen(operatingHours.getSunOpen().toString());
                dto.setClose(operatingHours.getSunClose().toString());
            }
        }

        // 2. 불가능 블록 조회
        List<CafeUnavailableBlock> blocks =
                unavailableRepo.findByCafeAndDayOfWeek(cafe, dayOfWeek);

        List<UnableTimeResponseDto.Block> blockDtos = blocks.stream().map(b -> {
            UnableTimeResponseDto.Block blockDto = new UnableTimeResponseDto.Block();
            blockDto.setStartTime(b.getStartTime().toString());
            blockDto.setEndTime(b.getEndTime().toString());
            return blockDto;
        }).toList();

        dto.setBlocks(blockDtos);

        return dto;
    }

    @Transactional
    public void changeOperatingStatus(Long cafeId, String type) {
        if (type == null || (!type.equalsIgnoreCase("on") && !type.equalsIgnoreCase("off"))) {
            throw new IllegalArgumentException("type 값은 'on' 또는 'off' 이어야 합니다.");
        }

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        CafeOperatingHours hours = cafeOperatingHoursRepository.findByCafe(cafe)
                .orElseThrow(() -> new IllegalArgumentException("등록된 운영시간 정보가 없습니다."));

        boolean isOpen = type.equalsIgnoreCase("on");

        hours.setMonIsOpen(isOpen);
        hours.setTueIsOpen(isOpen);
        hours.setWedIsOpen(isOpen);
        hours.setThuIsOpen(isOpen);
        hours.setFriIsOpen(isOpen);
        hours.setSatIsOpen(isOpen);
        hours.setSunIsOpen(isOpen);

        cafeOperatingHoursRepository.save(hours);
    }

    // 카페 예약 가능 시간 반환
    public CafeAbleTimeRequestDto getReservationTime(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));
        return CafeAbleTimeRequestDto.fromEntity(cafe);
    }

}
