package com.workat.domain.chat.repository.message;

import static com.workat.domain.chat.entity.QChatMessage.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ChatMessage> findLatestMessage(ChatRoom chatRoom, long messageId, long pageSize) {
		List<ChatMessage> result = jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId()).and(chatMessage.id.lt(messageId)))
			.orderBy(chatMessage.id.asc())
			.limit(pageSize)
			.fetch();
		log.info("findLatestMessages get messageId : " + messageId);
		log.info("findLatestMessages size: " + result.size());
		return result;
	}

	@Override
	public List<ChatMessage> findRecentMessage(ChatRoom chatRoom, long messageId, long pageSize) {
		List<ChatMessage> result = jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId()).and(chatMessage.id.gt(messageId)))
			.orderBy(chatMessage.id.asc())
			.limit(pageSize)
			.fetch();
		log.info("findLatestMessages get messageId : " + messageId);
		log.info("findLatestMessages size: " + result.size());
		return result;
	}

	@Override
	public boolean isAllMessageRead(long lastMessageId, long otherUserId) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.id.gt(lastMessageId).and(chatMessage.writerId.eq(otherUserId)))
			.stream().findAny().isEmpty();
	}
}
