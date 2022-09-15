package com.workat.domain.chat.repository.message;

import static com.workat.domain.chat.entity.QChatMessage.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
	public Optional<ChatMessage> findLastMessage(ChatRoom chatRoom, long userId) {
		return Optional.ofNullable(jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId())
				.and(chatMessage.id.gt(userId == chatRoom.getOwner().getId() ?
					chatRoom.getOwnerLastDeletedMessageId() : chatRoom.getApplicantLastDeletedMessageId())))
			.orderBy(chatMessage.id.desc())
			.limit(1)
			.fetchOne());
	}

	@Override
	public List<ChatMessage> findInitMessage(ChatRoom chatRoom, long userId, long messageId, long pageSize) {
		ArrayList<ChatMessage> result = new ArrayList<>();

		List<ChatMessage> beforeMessageId = jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId())
				.and(chatMessage.id.gt(getDeletedLastMessageId(chatRoom, userId)))
				.and(chatMessage.id.loe(messageId)))
			.orderBy(chatMessage.id.desc())
			.limit(pageSize + 1)
			.fetch();
		Collections.reverse(beforeMessageId);

		List<ChatMessage> afterMessageId = jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId())
				.and(chatMessage.id.gt(getDeletedLastMessageId(chatRoom, userId)))
				.and(chatMessage.id.gt(messageId)))
			.orderBy(chatMessage.id.asc())
			.limit(pageSize)
			.fetch();

		result.addAll(beforeMessageId);
		result.addAll(afterMessageId);

		return result;
	}

	@Override
	public List<ChatMessage> findLatestMessage(ChatRoom chatRoom, long userId, long messageId, long pageSize) {
		List<ChatMessage> result = jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId())
				.and(chatMessage.id.gt(getDeletedLastMessageId(chatRoom, userId)))
				.and(chatMessage.id.lt(messageId)))
			.orderBy(chatMessage.id.desc())
			.limit(pageSize)
			.fetch();
		Collections.reverse(result);

		return result;
	}

	@Override
	public List<ChatMessage> findRecentMessage(ChatRoom chatRoom, long userId, long messageId, long pageSize) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.room.id.eq(chatRoom.getId())
				.and(chatMessage.id.gt(getDeletedLastMessageId(chatRoom, userId)))
				.and(chatMessage.id.gt(messageId)))
			.orderBy(chatMessage.id.asc())
			.limit(pageSize)
			.fetch();
	}

	@Override
	public boolean isAllMessageRead(long chatRoomId, long lastMessageId) {
		return jpaQueryFactory.selectFrom(chatMessage)
			.where(chatMessage.id.gt(lastMessageId).and(chatMessage.room.id.eq(chatRoomId)))
			.stream().findAny().isEmpty();
	}

	private long getDeletedLastMessageId(ChatRoom chatRoom, long userId) {
		return userId == chatRoom.getOwner().getId() ? chatRoom.getOwnerLastDeletedMessageId() :
			chatRoom.getApplicantLastDeletedMessageId();
	}
}
