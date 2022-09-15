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
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.ChatMessageNotFoundException;
import com.workat.common.exception.ChatMessageRoomNotMatchException;
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
import com.workat.domain.user.repository.blocking.UserBlockingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

	private long pageSize = 50L;

	private final UsersRepository usersRepository;

	private final UserProfileRepository userProfileRepository;

	private final UserBlockingRepository userBlockingRepository;

	private final ChatRoomRepository chatRoomRepository;

	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public Long createChatRoom(Long ownerUserId, Long applyUserId) {
		if (ownerUserId.equals(applyUserId)) {
			throw new BadRequestException("자기 자신과 채팅룸을 만들 수 없습니다.");
		}

		Users findOwnerUser = usersRepository.findById(ownerUserId).orElseThrow(() -> {
			throw new UserNotFoundException(ownerUserId);
		});

		Users findApplyUser = usersRepository.findById(applyUserId).orElseThrow(() -> {
			throw new UserNotFoundException(applyUserId);
		});

		ChatRoom chatRoom = chatRoomRepository.findByUserIds(ownerUserId, applyUserId).orElse(ChatRoom.of());

		if (chatRoom.isFirstCreated()) {
			chatRoom.assignUsers(findOwnerUser, findApplyUser);
		} else {
			chatRoom.revivalRoom(applyUserId);
		}

		return chatRoomRepository.save(chatRoom).getId();
	}

	@Transactional(readOnly = true)
	public ChatRoomResponse getChatRooms(String baseUrl, Long userId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		List<Long> blockUserIdList = userBlockingRepository.findAllByReportingUserId(userId).stream()
			.map(userBlocking -> userBlocking.getBlockedUser().getId())
			.collect(Collectors.toList());

		List<ChatRoomDto> roomDtoList = chatRoomRepository.findAllByUser(findUser).stream()
			.filter(chatRoom -> !blockUserIdList.contains(chatRoom.getAnotherOwnerUserId(userId)))
			.filter(chatRoom -> !chatRoom.isDeletedByMe(userId))
			.map(chatRoom -> {
				Long anotherUserId = chatRoom.getAnotherOwnerUserId(findUser.getId());

				UserProfile anotherUserProfile = userProfileRepository.findById(anotherUserId).orElseThrow(() -> {
					throw new NotFoundException("another user profile is not found");
				});

				boolean isBlocked = !userBlockingRepository.findByReportingUserIdAndBlockedUserId(anotherUserId, userId)
					.isEmpty();

				String userProfileUrl =
					anotherUserProfile.getImageUrl() != null ? baseUrl + anotherUserProfile.getImageUrl() : null;

				ChatRoomListUserDto userDto = ChatRoomListUserDto.builder()
					.isOwner(chatRoom.isOwner(anotherUserId))
					.userId(anotherUserId)
					.userNickname(anotherUserProfile.getNickname())
					.position(anotherUserProfile.getPosition().getType())
					.workingYear(anotherUserProfile.getWorkingYear().getType())
					.userProfileUrl(userProfileUrl)
					.build();

				boolean isAllRead = chatMessageRepository.isAllMessageRead(chatRoom.getId(),
					chatRoom.getUsersLastCheckingMessageId(userId));

				ChatMessage findLastMessage = chatMessageRepository.findLastMessage(chatRoom, userId).orElse(null);

				return ChatRoomDto.builder()
					.id(chatRoom.getId())
					.lastMessageId(findLastMessage == null ? null : findLastMessage.getId())
					.lastMessage(findLastMessage == null ? null : findLastMessage.getMessage())
					.otherUser(userDto)
					.isStart(chatRoom.isStart())
					.isAllRead(isAllRead)
					.isBlockedByOtherUser(isBlocked)
					.createdDate(chatRoom.getCreatedDate())
					.build();
			})
			.collect(Collectors.toList());

		return ChatRoomResponse.of(roomDtoList);
	}

	@Transactional
	public void deleteChatRoom(Long userId, Long chatRoomId, Long lastMessageId) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new UserNotFoundException(userId);
		});

		ChatRoom findRoom = getChatRoomFromRepository(chatRoomId);

		validateChatRoom(findRoom, findUser.getId());

		ChatMessage findChatMessage = chatMessageRepository.findById(lastMessageId).orElseThrow(() -> {
			throw new ChatMessageNotFoundException(lastMessageId);
		});

		validateChatMessage(findChatMessage, findRoom.getId());

		findRoom.deleteRoom(userId, lastMessageId);

		if (findRoom.isDeletedByMe(userId) && findRoom.isDeletedByOther(userId)) {
			chatRoomRepository.deleteById(findRoom.getId());
		}

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
		chatMessageRepository.save(chatMessage);

		findRoom.setMessageUpdatedTime();
		findRoom.setOthersDeleted(writerId);
		findRoom.setUsersLastCheckingMessageId(writerId, chatMessage.getId());
		chatRoomRepository.save(findRoom);

		return chatMessage.getId();
	}

	@Transactional(readOnly = true)
	public ChatMessageResponse getChatMessages(Users user, Long chatRoomId, Long messageId,
		ChatMessageSortType sortType) {
		ChatRoom findRoom = getChatRoomFromRepository(chatRoomId);

		long value = messageId == null ? findRoom.getUsersLastCheckingMessageId(user.getId()) : messageId;
		log.info("messageId : " + messageId + ", value : " + value);

		List<ChatMessage> result;
		if (sortType == null || sortType == ChatMessageSortType.INIT) {
			result = chatMessageRepository.findInitMessage(findRoom, user.getId(), value, pageSize);
		} else if (sortType == ChatMessageSortType.AFTER) {
			result = chatMessageRepository.findRecentMessage(findRoom, user.getId(), value, pageSize);
		} else if (sortType == ChatMessageSortType.BEFORE) {
			result = chatMessageRepository.findLatestMessage(findRoom, user.getId(), value, pageSize);
		} else {
			throw new InvalidParameterException("chat message sort type not valid");
		}

		log.info("getChatMessages result size : " + result.size());

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

		return room;
	}

	private void validateChatRoom(ChatRoom chatRoom, Long userId) {
		if (!chatRoom.getOwner().getId().equals(userId) && !chatRoom.getApplicant().getId().equals(userId)) {
			throw new ChatRoomUserNotMatchException(chatRoom.getId(), userId);
		}
	}

	private void validateChatMessage(ChatMessage chatMessage, Long roomId) {
		if (!chatMessage.getRoom().getId().equals(roomId)) {
			throw new ChatMessageRoomNotMatchException(chatMessage.getRoom().getId(), roomId);
		}
	}

}
