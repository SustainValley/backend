package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ChatRoom  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;
    LocalDateTime lastMessageTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    @Builder
    public ChatRoom(Cafe cafe) {
        this.lastMessageTime = LocalDateTime.now();
        this.cafe = cafe;
    }

}
