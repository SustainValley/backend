package com.likelion.hackathon.controller;

import com.likelion.hackathon.apiPayload.ApiResponse;
import com.likelion.hackathon.apiPayload.exception.handler.ChatHandler;
import com.likelion.hackathon.dto.ChatDto.ChatRoomDto;
import com.likelion.hackathon.dto.ChatDto.ChatMessageDto;
import com.likelion.hackathon.redis.RedisPublisher;
import com.likelion.hackathon.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;

    // 모든 채팅방 조회
    @GetMapping("/{userId}")
    public ApiResponse<List<ChatRoomDto.ChatRoomResponseDto>> findAllChatRoom(@PathVariable("userId") Long id) {
        List<ChatRoomDto.ChatRoomResponseDto> chatRoomResponseDtoList = chatService.findAllChatRoom(id);
        return ApiResponse.onSuccess(chatRoomResponseDtoList);
    }

    // 이전 채팅 내용 조회
    @GetMapping("/room")
    public ApiResponse<List<ChatMessageDto.ChatMessageResponseDto>> findAllChatMessage(@RequestParam("id") Long id) {
        List<ChatMessageDto.ChatMessageResponseDto> chatMessageResponseDtos = chatService.findChatMessages(id);
        return ApiResponse.onSuccess(chatMessageResponseDtos);
    }

    // 채팅방 생성
    @PostMapping("/room/create")
    public ApiResponse<ChatRoomDto.ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomDto.ChatRoomRequestDto chatRoomRequestDto) {
        ChatRoomDto.ChatRoomResponseDto chatRoomResponseDto = chatService.createRoom(chatRoomRequestDto);
        return ApiResponse.onSuccess(chatRoomResponseDto);
    }



    // 메세지 전송 및 저장
    @MessageMapping("/message")
    public void receiveMessageRedis(ChatMessageDto.ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessageDto.ChatMessageResponseDto dto = chatService.createMessage(chatMessageRequestDto);
        redisPublisher.publish("/sub/chatroom/" + dto.getRoomId(), dto);
    }



}