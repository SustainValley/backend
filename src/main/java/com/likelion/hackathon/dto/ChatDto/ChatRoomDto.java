package com.likelion.hackathon.dto.ChatDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChatRoomDto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomResponseDto{
        private Long roomId;
        private LocalDateTime lastMessageTime;
        private Long chatRoomUserId;
        private String title;
        private boolean unread;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomRequestDto{
        private Long userId;
        private Long storeUserId;
    }
}
