package com.likelion.hackathon.entity.enums;

import lombok.Getter;

@Getter
public enum CancelReason {
    CLOSED_TIME("해당 시간대 예약 마감"),
    OUT_OF_BUSINESS("영업시간 외 예약요청"),
    CROWDED("매장 혼잡"),
    EQUIPMENT_UNAVAILABLE("요청 장비 사용 불가"),
    MAINTENANCE("시설 점검"),
    PEAK_LIMIT("피크타임 인원 제한"),
    NO_SHOW("고객 노쇼");

    private final String description;

    CancelReason(String description) {
        this.description = description;
    }
}
