package com.workat.api.chat.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.chat.dto.ChatMessageDto;
import com.workat.api.chat.dto.ChatRoomDto;
import com.workat.api.chat.dto.response.ChatMessageResponse;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.common.exception.ChatRoomIsDeletedException;
import com.workat.common.exception.ChatRoomNotFoundException;
import com.workat.common.exception.ChatRoomUserNotMatchException;
import com.workat.common.exception.UserNotFoundException;
import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatMessageSortType;
import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.chat.repository.message.ChatMessageRepository;
import com.workat.domain.chat.repository.room.ChatRoomRepository;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private long pageSize = 50L;

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
	public void deleteChatRoom(Long userId, Long chatRoomId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		ChatRoom findRoom = getChatRoomFromRepository(chatRoomId);

		validateChatRoom(findRoom, findUser.getId());

		findRoom.deleteRoom();

		chatRoomRepository.save(findRoom);
	}

	@Transactional
	public Long createChatMessage(Long chatRoomId, Long writerId, String message) {
		usersRepository.findById(writerId).orElseThrow(() -> {
			throw new UserNotFoundException(writerId);
		});

		ChatRoom findRoom = getChatRoomFromRepository(chatRoomId);

		validateChatRoom(findRoom, writerId);

		ChatMessage chatMessage = ChatMessage.of(writerId, message);
		chatMessage.assignRoom(findRoom);
		return chatMessageRepository.save(chatMessage).getId();
	}

	@Transactional(readOnly = true)
	public ChatMessageResponse getChatMessages(Long chatRoomId, Long messageId, ChatMessageSortType sortType) {
		ChatRoom findRoom = getChatRoomFromRepository(chatRoomId);

		List<ChatMessage> result;
		if (sortType == ChatMessageSortType.AFTER) {
			result = chatMessageRepository.findRecentMessage(findRoom, messageId, pageSize);
		} else if (sortType == ChatMessageSortType.BEFORE) {
			result = chatMessageRepository.findLatestMessage(findRoom, messageId, pageSize);
		} else {
			throw new InvalidParameterException("chat message sort type not valid");
		}

		return ChatMessageResponse.of(result.stream()
			.map(message -> ChatMessageDto.of(message.getId(), message.getWriterId(), message.getMessage(),
				message.getCreatedDate()))
			.collect(Collectors.toList()));
	}

	private ChatRoom getChatRoomFromRepository(Long roomId) {
		ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> {
			throw new ChatRoomNotFoundException(roomId);
		});

		if (room.isDeleted()) {
			throw new ChatRoomIsDeletedException(roomId);
		}

		return room;
	}

	private void validateChatRoom(ChatRoom chatRoom, Long userId) {
		if (!chatRoom.getOwner().getId().equals(userId) || !chatRoom.getOther().getId().equals(userId)) {
			throw new ChatRoomUserNotMatchException(chatRoom.getId(), userId);
		}
	}
}
