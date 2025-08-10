package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.ChatMessage;
import com.likelion.hackathon.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId(Long id);
}
