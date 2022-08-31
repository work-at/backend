package com.workat.domain.chat.repository.message;

import java.util.List;
import java.util.Optional;

import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatRoom;

public interface CustomChatMessageRepository {

	Optional<ChatMessage> findLastMessage(ChatRoom chatRoom, long userId);

	List<ChatMessage> findInitMessage(ChatRoom chatRoom, long userId, long messageId, long pageSize);

	List<ChatMessage> findLatestMessage(ChatRoom chatRoom, long userId, long messageId, long pageSize);

	List<ChatMessage> findRecentMessage(ChatRoom chatRoom, long userId, long messageId, long pageSize);

	boolean isAllMessageRead(long lastMessageId, long otherUserId);
}
