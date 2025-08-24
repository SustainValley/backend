package com.likelion.hackathon.dto.CafeDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class UnableTimeResponseDto {
    private String open;
    private String close;
    private List<Block> blocks;

    @Getter
    @Setter
    public static class Block {
        private String startTime;
        private String endTime;
    }
}