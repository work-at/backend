package com.workat.domain.chat.repository.room;

import java.util.List;
import java.util.Optional;

import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.user.entity.Users;

public interface ChatRoomCustomRepository {

	Optional<ChatRoom> findByUserIds(Long owner, Long applicant);

	List<ChatRoom> findAllByUser(Users user);
}
