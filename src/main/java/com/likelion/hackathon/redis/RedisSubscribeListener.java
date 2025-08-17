package com.likelion.hackathon.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.hackathon.dto.ChatDto.ChatMessageDto;
import com.likelion.hackathon.entity.ChatRoomUser;
import com.likelion.hackathon.repository.ChatRoomRepository;
import com.likelion.hackathon.repository.ChatRoomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscribeListener implements MessageListener {

    private final RedisTemplate<String, Object> template;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = template
                    .getStringSerializer().deserialize(message.getBody());

            ChatMessageDto.ChatMessageResponseDto messageDto = objectMapper.readValue(publishMessage, ChatMessageDto.ChatMessageResponseDto.class);

            log.info("Redis Subscribe Channel : " + messageDto.getRoomId());
            log.info("Redis SUB Message : {}", publishMessage);

            // 채팅방  업데이트
            chatRoomRepository.findById(messageDto.getRoomId())
                    .ifPresent(chatRoom -> {
                        chatRoom.setLastMessageTime(LocalDateTime.now());
                        chatRoomRepository.save(chatRoom);
                    });

            // 안읽음 카운트 증가 (보낸 사람 제외)
            List<ChatRoomUser> participants = chatRoomUserRepository.findByRoomId(messageDto.getRoomId());
            for (ChatRoomUser cru : participants) {
                if (!cru.getUser().getId().equals(messageDto.getSender())) {
                    String key = "chat:unread:" + messageDto.getRoomId() + ":" + cru.getUser().getId();
                    System.out.println("sender id = " + cru.getId());
                    template.opsForValue().increment(key);
                }
            }


            // 메세지 구독자들에게 전송
            messagingTemplate.convertAndSend("/sub/chatroom/" + messageDto.getRoomId(), messageDto);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
