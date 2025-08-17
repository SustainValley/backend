package com.likelion.hackathon.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SpaceType {
    OPEN("오픈된 공간 (다른 이용자와 함께 이용)"),
    QUIET("조용한 공간 (집중 업무용)"),
    MEETING("회의실 (분리된 공간)"),
    LIMITED_TALK("제한적 대화 공간 (소음 최소화)");

    private final String description;

    SpaceType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static SpaceType fromDescription(String description) {
        for (SpaceType type : SpaceType.values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SpaceType: " + description);
    }
}
