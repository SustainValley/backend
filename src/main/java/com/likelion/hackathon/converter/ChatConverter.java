package com.likelion.hackathon.converter;

import com.likelion.hackathon.dto.ChatDto.ChatRoomDto;
import com.likelion.hackathon.dto.ChatDto.ChatMessageDto;
import com.likelion.hackathon.entity.ChatMessage;
import com.likelion.hackathon.entity.ChatRoom;
import com.likelion.hackathon.entity.ChatRoomUser;
import com.likelion.hackathon.entity.User;

import java.time.format.DateTimeFormatter;

public class ChatConverter {

    public static ChatMessageDto.ChatMessageResponseDto toChatMessageDto(ChatMessage chatMessage) {
        return ChatMessageDto.ChatMessageResponseDto.builder()
                .message(chatMessage.getMessage())
                .roomId(chatMessage.getRoom().getId())
                .sender(chatMessage.getSender().getId())
                .createdAt(chatMessage.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static ChatMessage toChatMessageEntity(ChatRoom room, ChatRoomUser sender, ChatMessageDto.ChatMessageRequestDto dto) {
        return ChatMessage.builder()
                .message(dto.getMessage())
                .room(room)
                .sender(sender)
                .build();
    }

    public static ChatRoomDto.ChatRoomCreateResponseDto toChatRoomCreateResponseDto(ChatRoomUser chatRoomUser, ChatRoomUser chatRoomUser2) {

        return ChatRoomDto.ChatRoomCreateResponseDto.builder()
                .roomId(chatRoomUser.getRoom().getId())
                .chatRoomUserId(chatRoomUser.getId())
                .chatRoomStoreUserId(chatRoomUser2.getId())
                .build();
    }

    public static ChatRoomDto.ChatRoomResponseDto toChatRoomResponseDto(ChatRoomUser chatRoomUser, String title, boolean unread, String lastMessage) {

        return ChatRoomDto.ChatRoomResponseDto.builder()
                .roomId(chatRoomUser.getRoom().getId())
                .chatRoomUserId(chatRoomUser.getId())
                .title(title)
                .lastMessage(lastMessage)
                .unread(unread)
                .build();
    }
}
