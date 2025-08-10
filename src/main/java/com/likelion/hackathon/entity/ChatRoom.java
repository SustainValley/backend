package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ChatRoom  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;
    LocalDateTime lastMessageTime;

    @Builder
    public ChatRoom() {
        this.lastMessageTime = LocalDateTime.now();
    }

}
