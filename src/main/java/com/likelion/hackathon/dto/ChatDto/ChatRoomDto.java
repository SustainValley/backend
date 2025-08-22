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
    public static class ChatRoomCreateResponseDto{
        private Long roomId;
        private Long chatRoomUserId;
        private Long chatRoomStoreUserId;
        private Long cafeId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomResponseDto{
        private Long roomId;
        private Long chatRoomUserId;
        private String title;
        private boolean unread;
        private String lastMessage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomRequestDto{
        private Long userId;
        private Long storeUserId;
        private Long cafeId;
    }
}
