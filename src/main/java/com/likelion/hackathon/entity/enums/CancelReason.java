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
    NO_SHOW("고객 노쇼"),

    SCHEDULE_CHANGE("일정변경으로 인한 취소"),
    PERSONAL_REASON("개인사정(긴급 용무 등)"),
    TIME_MISTAKE("예약시간 착오"),
    LOCATION_CHANGE("장소 변경"),
    LACK_OF_ATTENDEES("참석 인원 부족"),
    BUDGET_ISSUE("비용, 예산 문제"),
    DUPLICATE("중복예약");

    private final String description;

    CancelReason(String description) {
        this.description = description;
    }
}
