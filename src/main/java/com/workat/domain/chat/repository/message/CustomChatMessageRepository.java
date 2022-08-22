package com.workat.domain.chat.repository.message;

import java.util.List;
import java.util.Optional;

import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatRoom;

public interface CustomChatMessageRepository {

	List<ChatMessage> findLatestMessage(ChatRoom chatRoom, long messageId, long pageSize);

	List<ChatMessage> findRecentMessage(ChatRoom chatRoom, long messageId, long pageSize);

	Optional<ChatMessage> findLastMessage(ChatRoom chatRoom);

	boolean isAllMessageRead(long lastMessageId, long otherUserId);
}
