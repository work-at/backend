package com.workat.domain.chat.repository.room;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository {

}
