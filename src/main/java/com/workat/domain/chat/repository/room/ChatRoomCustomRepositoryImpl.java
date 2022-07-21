package com.workat.domain.chat.repository.room;

import static com.workat.domain.chat.entity.QChatRoom.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ChatRoom> findAllByUser(Users user) {
		return queryFactory.selectFrom(chatRoom)
			.where(
				chatRoom.user1.eq(user).or(chatRoom.user2.eq(user))
			).fetch();
	}
}
