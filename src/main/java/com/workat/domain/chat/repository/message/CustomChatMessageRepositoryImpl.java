package com.workat.domain.chat.repository.message;

import static com.workat.domain.chat.entity.QChatMessage.*;

import java.util.List;

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
			.where(chatMessage.room.eq(chatRoom).and(chatMessage.id.lt(messageId)))
			.orderBy(chatMessage.id.desc())
			.limit(pageSize)
			.fetch();
	}

	@Override
	public List<ChatMessage> findRecentMessage(ChatRoom chatRoom, long messageId, long pageSize) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.eq(chatRoom).and(chatMessage.id.gt(messageId)))
			.orderBy(chatMessage.id.asc())
			.limit(pageSize)
			.fetch();
	}

	@Override
	public boolean isAllMessageRead(long lastMessageId, long otherUserId) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.id.gt(lastMessageId).and(chatMessage.writerId.eq(otherUserId)))
			.stream().findAny().isEmpty();
	}
}
