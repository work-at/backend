package com.workat.domain.chat.repository.room;

import java.util.List;

import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.user.entity.Users;

public interface ChatRoomCustomRepository {

	List<ChatRoom> findAllByUser(Users user);
}
