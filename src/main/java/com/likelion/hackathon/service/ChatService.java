package com.likelion.hackathon.service;

import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.handler.ChatHandler;
import com.likelion.hackathon.converter.ChatConverter;
import com.likelion.hackathon.dto.ChatDto.ChatRoomDto;
import com.likelion.hackathon.dto.ChatDto.ChatMessageDto;
import com.likelion.hackathon.entity.ChatMessage;
import com.likelion.hackathon.entity.ChatRoom;
import com.likelion.hackathon.entity.ChatRoomUser;
import com.likelion.hackathon.entity.User;
import com.likelion.hackathon.repository.ChatMessageRepository;
import com.likelion.hackathon.repository.ChatRoomRepository;
import com.likelion.hackathon.repository.ChatRoomUserRepository;
import com.likelion.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;


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

        if (! userRepository.existsById(userId)) {
            throw new ChatHandler(ErrorStatus._USER_NOT_FOUND);
        }
        List<Object[]> result = chatRoomUserRepository.findChatRoomsWithOtherUserByUserId(userId);

        return result.stream()
                .map(row -> {
                    ChatRoomUser cru = (ChatRoomUser) row[0];
                    String otherUserName = (String) row[1];
                    return ChatConverter.toChatRoomResponseDto(cru, otherUserName);
                })
                .toList();
    }

    // 채팅방 생성 메서드
    // 메세지
    public ChatRoomDto.ChatRoomResponseDto createRoom(ChatRoomDto.ChatRoomRequestDto chatRoomRequestDto) {
        Optional<User> optionalUser1 = userRepository.findById(chatRoomRequestDto.getUserId());
        Optional<User> optionalUser2 = userRepository.findById(chatRoomRequestDto.getStoreUserId());

        if (optionalUser1.isEmpty() || optionalUser2.isEmpty()) {

            System.out.println("optionalUser1.isEmpty() || optionalUser2.isEmpty()");
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

        ChatRoom chatRoom = new ChatRoom();

        chatRoomRepository.save(chatRoom);

        // 유저 저장
        ChatRoomUser chatRoomUser1 = createChatRoomUser(user1, chatRoom);
        ChatRoomUser chatRoomUser2 = createChatRoomUser(user2, chatRoom);


        return ChatConverter.toChatRoomResponseDto(chatRoomUser1, user2.getUsername());
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

//
//    public <T> void sendMessage(WebSocketSession receiver, ChatMessage message) {
//        try {
//            if(receiver != null && receiver.isOpen()) { // 타켓이 존재하고, 연결된 상태라면 메세지 전송
//                receiver.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
//            }
//        }
//        catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public List<ChatRoom> findAllRoom() {
//        return new ArrayList<>(chatRooms.values());
//    }
//
//    public ChatRoom findRoomById(String roomId) {
//        return chatRooms.get(roomId);
//    }
}
