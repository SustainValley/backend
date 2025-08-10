package com.likelion.hackathon.redis;

import com.likelion.hackathon.dto.ChatDto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    //redis 메시지 발행

    private final RedisTemplate<String, Object> template;


    /**
     * Object publish
     */
    public void publish(String topic, ChatMessageDto.ChatMessageResponseDto dto) {
        template.convertAndSend(topic, dto);
    }

    /**
     * String publish
     */
    public void publish(ChannelTopic topic ,String data) {
        template.convertAndSend(topic.getTopic(), data);
    }
}

