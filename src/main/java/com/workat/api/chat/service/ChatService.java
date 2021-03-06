package com.workat.api.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.chat.dto.ChatMessageDto;
import com.workat.api.chat.dto.ChatRoomDto;
import com.workat.api.chat.dto.response.ChatMessageResponse;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.common.exception.ChatRoomNotFoundException;
import com.workat.common.exception.UserNotFoundException;
import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.chat.repository.message.ChatMessageRepository;
import com.workat.domain.chat.repository.room.ChatRoomRepository;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final UsersRepository usersRepository;

	private final ChatRoomRepository chatRoomRepository;

	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public Long createChatRoom(Long ownerUserId, Long otherUserId) {
		Users findOwnerUser = usersRepository.findById(ownerUserId).orElseThrow(() -> {
			throw new UserNotFoundException(ownerUserId);
		});
		Users findOtherUser = usersRepository.findById(otherUserId).orElseThrow(() -> {
			throw new UserNotFoundException(otherUserId);
		});

		ChatRoom chatRoom = ChatRoom.of();
		chatRoom.assignUsers(findOwnerUser, findOtherUser);

		return chatRoomRepository.save(chatRoom).getId();
	}

	@Transactional(readOnly = true)
	public ChatRoomResponse getChatRooms(Long userId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		List<ChatRoomDto> roomDtos = chatRoomRepository.findAllByUser(findUser).stream()
			.map(chatRoom -> ChatRoomDto.of(chatRoom.getId(), chatRoom.getOwner().getId(), chatRoom.getOther().getId(),
				chatRoom.getCreatedDate()))
			.collect(Collectors.toList());

		return ChatRoomResponse.of(roomDtos);
	}

	@Transactional
	public Long createChatMessage(Long chatRoomId, Long writerId, String message) {
		usersRepository.findById(writerId).orElseThrow(() -> {
			throw new UserNotFoundException(writerId);
		});

		ChatRoom findRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> {
			throw new ChatRoomNotFoundException(chatRoomId);
		});

		ChatMessage chatMessage = ChatMessage.of(writerId, message);
		chatMessage.assignRoom(findRoom);
		return chatMessageRepository.save(chatMessage).getId();
	}

	@Transactional(readOnly = true)
	public ChatMessageResponse getChatMessages(Long chatRoomId, Pageable pageable) {
		ChatRoom findRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> {
			throw new ChatRoomNotFoundException(chatRoomId);
		});

		return ChatMessageResponse.of(chatMessageRepository.findAllByRoomOrderByCreatedDateDesc(findRoom, pageable)
			.map(message -> ChatMessageDto.of(message.getId(), message.getWriterId(), message.getMessage(),
				message.getCreatedDate())));
	}
}
