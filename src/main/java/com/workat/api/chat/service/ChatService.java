package com.workat.api.chat.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.chat.dto.ChatMessageDto;
import com.workat.api.chat.dto.ChatRoomDto;
import com.workat.api.chat.dto.ChatRoomListUserDto;
import com.workat.api.chat.dto.response.ChatMessageResponse;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.common.exception.ChatRoomIsDeletedException;
import com.workat.common.exception.ChatRoomNotFoundException;
import com.workat.common.exception.ChatRoomUserNotMatchException;
import com.workat.common.exception.NotFoundException;
import com.workat.common.exception.UserNotFoundException;
import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatMessageSortType;
import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.chat.repository.message.ChatMessageRepository;
import com.workat.domain.chat.repository.room.ChatRoomRepository;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private long pageSize = 50L;

	private final UsersRepository usersRepository;

	private final UserProfileRepository userProfileRepository;

	private final ChatRoomRepository chatRoomRepository;

	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public Long createChatRoom(Long ownerUserId, Long applyUserId) {
		Users findOwnerUser = usersRepository.findById(ownerUserId).orElseThrow(() -> {
			throw new UserNotFoundException(ownerUserId);
		});
		Users findApplyUser = usersRepository.findById(applyUserId).orElseThrow(() -> {
			throw new UserNotFoundException(applyUserId);
		});

		ChatRoom chatRoom = ChatRoom.of();
		chatRoom.assignUsers(findOwnerUser, findApplyUser);

		return chatRoomRepository.save(chatRoom).getId();
	}

	@Transactional(readOnly = true)
	public ChatRoomResponse getChatRooms(Long userId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		List<ChatRoomDto> roomDtoList = chatRoomRepository.findAllByUser(findUser).stream()
			.filter(chatRoom -> !chatRoom.isDeletedByMe(userId))
			.map(chatRoom -> {
				Long anotherUserId = chatRoom.getAnotherOwnerUserId(findUser.getId());

				UserProfile anotherUserProfile = userProfileRepository.findById(anotherUserId).orElseThrow(() -> {
					throw new NotFoundException("another user profile is not found");
				});

				ChatRoomListUserDto userDto = ChatRoomListUserDto.builder()
					.userId(anotherUserId)
					.userNickname(anotherUserProfile.getNickname())
					.position(anotherUserProfile.getPosition().getType())
					.workingYear(anotherUserProfile.getWorkingYear().getType())
					.userProfileUrl(anotherUserProfile.getImageUrl())
					.build();

				boolean isAllRead = chatMessageRepository.isAllMessageRead(
					chatRoom.getUsersLastCheckingMessageId(userId), anotherUserId);

				return ChatRoomDto.builder()
					.id(chatRoom.getId())
					.otherUser(userDto)
					.isStart(chatRoom.isStart())
					.isAllRead(isAllRead)
					.isDeletedByOtherUser(chatRoom.isDeletedByOther(userId))
					.isBlockedByOtherUser(false)
					.createdDate(chatRoom.getCreatedDate())
					.build();
			})
			.collect(Collectors.toList());

		return ChatRoomResponse.of(roomDtoList);
	}

	@Transactional
	public void deleteChatRoom(Long userId, Long chatRoomId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		ChatRoom findRoom = getChatRoomFromRepository(chatRoomId);

		validateChatRoom(findRoom, findUser.getId());

		findRoom.deleteRoom(userId);

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

	@Transactional
	public void postRoomLastUserCheckingMessage(Long userId, Long roomId, Long lastMessageId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		ChatRoom findRoom = getChatRoomFromRepository(roomId);

		validateChatRoom(findRoom, findUser.getId());

		findRoom.setUsersLastCheckingMessageId(findUser.getId(), lastMessageId);

		chatRoomRepository.save(findRoom);
	}

	@Transactional
	public void chattingConfirm(Long userId, Long roomId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		ChatRoom findRoom = getChatRoomFromRepository(roomId);

		validateChatRoom(findRoom, findUser.getId());

		findRoom.chattingConfirm(userId);

		chatRoomRepository.save(findRoom);
	}

	private ChatRoom getChatRoomFromRepository(Long roomId) {
		ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> {
			throw new ChatRoomNotFoundException(roomId);
		});

		if (room.isOwnerDeleted() || room.isApplicantDeleted()) {
			throw new ChatRoomIsDeletedException(roomId);
		}

		return room;
	}

	private void validateChatRoom(ChatRoom chatRoom, Long userId) {
		if (!chatRoom.getOwner().getId().equals(userId) && !chatRoom.getApplicant().getId().equals(userId)) {
			throw new ChatRoomUserNotMatchException(chatRoom.getId(), userId);
		}
	}

}
