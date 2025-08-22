package com.likelion.hackathon.service;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.ChatHandler;
import com.likelion.hackathon.converter.ChatConverter;
import com.likelion.hackathon.dto.ChatDto.ChatRoomDto;
import com.likelion.hackathon.dto.ChatDto.ChatMessageDto;
import com.likelion.hackathon.entity.*;
import com.likelion.hackathon.repository.ChatMessageRepository;
import com.likelion.hackathon.repository.ChatRoomRepository;
import com.likelion.hackathon.repository.ChatRoomUserRepository;
import com.likelion.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final CafeService cafeService;


    // 채팅방 내 모든 메세지 찾는 메서드
    public List<ChatMessageDto.ChatMessageResponseDto> findChatMessages(Long id) {

        // 채팅방 있는지 여부 조회

        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new ChatHandler(ErrorStatus._CHAT_ROOM_NOT_FOUND));

        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByRoomId(chatRoom.getId());

        return chatMessageList.stream()
                .map(ChatConverter::toChatMessageDto)
                .toList();
    }

    // 모든 채팅방 찾는 메서드
    public List<ChatRoomDto.ChatRoomResponseDto> findAllChatRoom(Long userId) {

        User user = userService.existUser(userId);

        if (user.getType() == UserType.COR){
            List<Object[]> result = chatRoomUserRepository
                    .findChatRoomsWithOtherUserAndLastMessageByUserId(userId);
            return result.stream()
                    .map(row -> {
                        ChatRoomUser cru = (ChatRoomUser) row[0];
                        String otherNickname = (String) row[1];
                        String lastMessage = row[2] != null ? (String) row[2] : "";

                        String key = "chat:unread:" + cru.getRoom().getId() + ":" + userId;
                        Object unreadCountObj = redisTemplate.opsForValue().get(key);
                        boolean hasUnread = unreadCountObj != null && Integer.parseInt(unreadCountObj.toString()) > 0;

                        return ChatConverter.toChatRoomResponseDto(cru, otherNickname, hasUnread, lastMessage);
                    })
                    .toList();
        }
        else {
            List<Object[]> result = chatRoomUserRepository
                    .findChatRoomsWithCafeNameAndLastMessageByUserId(userId);

            return result.stream()
                    .map(row -> {
                        ChatRoomUser cru = (ChatRoomUser) row[0];
                        String cafeName = (String) row[1];
                        String lastMessage = row[2] != null ? (String) row[2] : "";

                        String key = "chat:unread:" + cru.getRoom().getId() + ":" + userId;
                        Object unreadCountObj = redisTemplate.opsForValue().get(key);
                        boolean hasUnread = unreadCountObj != null && Integer.parseInt(unreadCountObj.toString()) > 0;

                        return ChatConverter.toChatRoomResponseDto(cru, cafeName, hasUnread, lastMessage);
                    })
                    .toList();

        }
    }

    @Transactional
    public void enterRoom(Long roomId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus._USER_NOT_FOUND));
        String key = "chat:unread:" + roomId + ":" + userId;
        redisTemplate.delete(key); // 초기화
    }

    // 채팅방 생성 메서드
    // 메세지
    public ChatRoomDto.ChatRoomCreateResponseDto createRoom(ChatRoomDto.ChatRoomRequestDto chatRoomRequestDto) {
        Optional<User> optionalUser1 = userRepository.findById(chatRoomRequestDto.getUserId());
        Optional<User> optionalUser2 = userRepository.findById(chatRoomRequestDto.getStoreUserId());

        if (optionalUser1.isEmpty() || optionalUser2.isEmpty()) {
            throw new ChatHandler(ErrorStatus._USER_NOT_FOUND);
        }

        User user1 = optionalUser1.get();
        User user2 = optionalUser2.get();

        // 이미 방이 있는지 체크
        Optional<ChatRoom> existingRoom = chatRoomUserRepository
                .findExistingRoomBetweenUsers(user1.getId(), user2.getId());

        if (existingRoom.isPresent()) {
            throw new ChatHandler(ErrorStatus._CHAT_ROOM_ALREADY_EXISTS);
        }

        ChatRoom chatRoom = ChatConverter.toChatRoomEntity(cafeService.findCafe(chatRoomRequestDto.getCafeId()));

        chatRoomRepository.save(chatRoom);

        // 유저 저장
        ChatRoomUser chatRoomUser1 = createChatRoomUser(user1, chatRoom);
        ChatRoomUser chatRoomUser2 = createChatRoomUser(user2, chatRoom);

        return ChatConverter.toChatRoomCreateResponseDto(chatRoomUser1, chatRoomUser2, chatRoomRequestDto.getCafeId());
    }

    // 채팅 참여한 유저 저장
    public ChatRoomUser createChatRoomUser(User user, ChatRoom chatRoom) {
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .room(chatRoom)
                .user(user)
                .build();

        return chatRoomUserRepository.save(chatRoomUser);
    }

    // 메세지 저장 메서드
    public ChatMessageDto.ChatMessageResponseDto createMessage(ChatMessageDto.ChatMessageRequestDto chatMessageRequestDto) {
        ChatRoom room = chatRoomRepository.findById(chatMessageRequestDto.getRoomId())
                .orElseThrow(() -> new ChatHandler(ErrorStatus._CHAT_ROOM_NOT_FOUND));

        ChatRoomUser sender = chatRoomUserRepository.findById(chatMessageRequestDto.getSender())
                .orElseThrow(() -> new ChatHandler(ErrorStatus._USER_NOT_FOUND));

        ChatMessage entity = ChatConverter.toChatMessageEntity(room, sender, chatMessageRequestDto);
        chatMessageRepository.save(entity);

        return ChatConverter.toChatMessageDto(entity);
    }

}
