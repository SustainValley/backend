package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.ChatRoom;
import com.likelion.hackathon.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    @Query("""
        SELECT cru, otherUser.nickname
        FROM ChatRoomUser cru
        JOIN cru.room r
        JOIN ChatRoomUser otherCru ON otherCru.room.id = r.id AND otherCru.user.id <> :userId
        JOIN otherCru.user otherUser
        WHERE cru.user.id = :userId
        ORDER BY r.lastMessageTime DESC
    """)
    List<Object[]> findChatRoomsWithOtherUserByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT cru, otherUser.nickname, m.message
    FROM ChatRoomUser cru
    JOIN cru.room r
    JOIN ChatRoomUser otherCru ON otherCru.room.id = r.id AND otherCru.user.id <> :userId
    JOIN otherCru.user otherUser
    LEFT JOIN ChatMessage m ON m.room.id = r.id
        AND m.createdAt = (SELECT MAX(m2.createdAt) 
                           FROM ChatMessage m2 
                           WHERE m2.room.id = r.id)
    WHERE cru.user.id = :userId
    ORDER BY r.lastMessageTime DESC
""")
    List<Object[]> findChatRoomsWithOtherUserAndLastMessageByUserId(@Param("userId") Long userId);
    @Query("""
        SELECT r
        FROM ChatRoom r
        JOIN ChatRoomUser cru1 ON cru1.room.id = r.id AND cru1.user.id = :userId1
        JOIN ChatRoomUser cru2 ON cru2.room.id = r.id AND cru2.user.id = :userId2
    """)
    Optional<ChatRoom> findExistingRoomBetweenUsers(@Param("userId1") Long userId1,
                                                    @Param("userId2") Long userId2);

    List<ChatRoomUser> findByRoomId(Long chatRoomId);
}
