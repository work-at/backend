package com.workat.domain.chat.repository.message;

import static com.workat.domain.chat.entity.QChatMessage.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatRoom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ChatMessage> findLatestMessage(ChatRoom chatRoom, long messageId, long pageSize) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId()).and(chatMessage.id.lt(messageId)))
			.orderBy(chatMessage.id.asc())
			.limit(pageSize)
			.fetch();
	}

	@Override
	public List<ChatMessage> findRecentMessage(ChatRoom chatRoom, long messageId, long pageSize) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId()).and(chatMessage.id.gt(messageId)))
			.orderBy(chatMessage.id.asc())
			.limit(pageSize)
			.fetch();
	}

	@Override
	public Optional<ChatMessage> findLastMessage(ChatRoom chatRoom) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId()))
			.orderBy(chatMessage.id.desc())
			.stream().findFirst();
	}

	@Override
	public boolean isAllMessageRead(long lastMessageId, long otherUserId) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.id.gt(lastMessageId).and(chatMessage.writerId.eq(otherUserId)))
			.stream().findAny().isEmpty();
	}
}
