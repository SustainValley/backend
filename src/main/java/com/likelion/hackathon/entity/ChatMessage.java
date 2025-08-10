package com.likelion.hackathon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ChatRoomUser sender;

    private String message;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage (ChatRoom room, ChatRoomUser sender, String message) {
        this.room = room;
        this.sender = sender;
        this.message = message;
    }

}
