package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByOrderByLastMessageTimeDesc();
}
