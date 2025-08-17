package com.likelion.hackathon.controller;

import com.likelion.hackathon.apiPayload.ApiResponse;
import com.likelion.hackathon.apiPayload.exception.handler.ChatHandler;
import com.likelion.hackathon.dto.ChatDto.ChatRoomDto;
import com.likelion.hackathon.dto.ChatDto.ChatMessageDto;
import com.likelion.hackathon.redis.RedisPublisher;
import com.likelion.hackathon.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "모든 채팅방 조회", description = "사용자의 모든 채팅방을 조회해 반환합니다.")
    @GetMapping("/{userId}")
    public ApiResponse<List<ChatRoomDto.ChatRoomResponseDto>> findAllChatRoom(@PathVariable("userId") Long id) {
        List<ChatRoomDto.ChatRoomResponseDto> chatRoomResponseDtoList = chatService.findAllChatRoom(id);
        return ApiResponse.onSuccess(chatRoomResponseDtoList);
    }

    // 이전 채팅 내용 조회
    @Operation(summary = "이전 채팅 메세지 조회", description = "id로 전달받은 채팅방의 이전 대화 기록을 조회해 반환합니다.")
    @GetMapping("/room")
    public ApiResponse<List<ChatMessageDto.ChatMessageResponseDto>> findAllChatMessage(@RequestParam("id") Long id) {
        List<ChatMessageDto.ChatMessageResponseDto> chatMessageResponseDtos = chatService.findChatMessages(id);
        return ApiResponse.onSuccess(chatMessageResponseDtos);
    }

    // 채팅방 생성
    @PostMapping("/room/create")
    @Operation(summary = "채팅방 생성", description = "새로운 채팅을 개설합니다.")
    public ApiResponse<ChatRoomDto.ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomDto.ChatRoomRequestDto chatRoomRequestDto) {
        ChatRoomDto.ChatRoomResponseDto chatRoomResponseDto = chatService.createRoom(chatRoomRequestDto);
        return ApiResponse.onSuccess(chatRoomResponseDto);
    }

    @PostMapping("/room/{roomId}/enter")
    @Operation(summary = "채팅 읽음 처리", description = "읽지 않은 메세지가 있는 채팅방 입장 시 채팅을 읽음으로 처리합니다. 입장 직후 한 번만 호출되어야 합니다.")
    public ApiResponse<String> enterRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatService.enterRoom(roomId, userId);
        return ApiResponse.onSuccess("success");
    }

    // 메세지 전송 및 저장
    @MessageMapping("/message")
    public void receiveMessageRedis(ChatMessageDto.ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessageDto.ChatMessageResponseDto dto = chatService.createMessage(chatMessageRequestDto);
        redisPublisher.publish("/sub/chatroom/" + dto.getRoomId(), dto);
    }



}