package com.workat.domain.chat.repository.message;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, CustomChatMessageRepository {
}
