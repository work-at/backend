package com.workat.api.chat.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.workat.api.chat.dto.ChatMessageDto;
import com.workat.api.chat.dto.ChatRoomDto;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.common.exception.ChatRoomNotFoundException;
import com.workat.common.exception.UserNotFoundException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.chat.entity.ChatMessage;
import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.chat.repository.message.ChatMessageRepository;
import com.workat.domain.chat.repository.room.ChatRoomRepository;
import com.workat.domain.config.DataJpaTestConfig;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UsersRepository;

@Import(DataJpaTestConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class ChatServiceTest {

	private ChatService chatService;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@BeforeEach
	void init() {
		this.chatService = new ChatService(usersRepository, chatRoomRepository, chatMessageRepository);
	}

	@Test
	void createChatRoom_success() {
		//given
		Users user1 = Users.of(OauthType.KAKAO, 1L);
		Users user2 = Users.of(OauthType.KAKAO, 2L);
		usersRepository.saveAll(List.of(user1, user2));

		Long chatRoomId = chatService.createChatRoom(user1.getId(), user2.getId());

		//when
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();

		//then
		assertAll(
			() -> assertEquals(chatRoom.getId(), chatRoomId),
			() -> assertEquals(chatRoom.getUser1(), user1),
			() -> assertEquals(chatRoom.getUser2(), user2)
		);
	}

	@Test
	void createChatRoom_fail_user_not_found() {
		//given

		//when

		//then
		assertThrows(UserNotFoundException.class, () -> chatService.createChatRoom(1L, 2L));
	}

	@Test
	void getChatRooms_success() {
		//given
		Users user1 = Users.of(OauthType.KAKAO, 1L);
		Users user2 = Users.of(OauthType.KAKAO, 2L);
		usersRepository.saveAll(List.of(user1, user2));

		Long chatRoomId = chatService.createChatRoom(user1.getId(), user2.getId());

		//when

		//then
		ChatRoomResponse resultChatRooms = chatService.getChatRooms(user1.getId());
		assertEquals(resultChatRooms.getRooms().size(), 1);

		ChatRoomDto chatRoom = resultChatRooms.getRooms().get(0);
		assertAll(
			() -> assertEquals(chatRoom.getId(), chatRoomId),
			() -> assertTrue(chatRoom.getOwnerUserIds().contains(user1.getId())),
			() -> assertTrue(chatRoom.getOwnerUserIds().contains(user2.getId()))
		);
	}

	@Test
	void getChatRooms_success_room_is_empty() {
		//given
		Users user = Users.of(OauthType.KAKAO, 1L);
		usersRepository.save(user);

		//when

		//then
		assertEquals(Collections.EMPTY_LIST, chatService.getChatRooms(user.getId()).getRooms());
	}

	@Test
	void getChatRooms_fail_user_not_found() {
		//given

		//when

		//then
		assertThrows(UserNotFoundException.class, () -> chatService.getChatRooms(1L));
	}

	@Test
	void createChatMessage_success_page_1() {
		//given
		Users user1 = Users.of(OauthType.KAKAO, 1L);
		Users user2 = Users.of(OauthType.KAKAO, 2L);
		usersRepository.saveAll(List.of(user1, user2));

		int inputSize = 100;
		int pageSize = 10;

		Long chatRoomId = chatService.createChatRoom(user1.getId(), user2.getId());

		ArrayList<Long> addedIds = new ArrayList<>();
		for (int i = 1; i <= inputSize; i++) {
			Long userId = (i % 2) != 0 ? user1.getId() : user2.getId();
			Long resultId = chatService.createChatMessage(chatRoomId, userId, "test" + i);
			addedIds.add(resultId);
		}
		Collections.sort(addedIds, Collections.reverseOrder());

		//when
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		Pageable pageable = PageRequest.of(0, pageSize, Sort.by("createdDate").descending());
		ArrayList<ChatMessage> chatMessages = new ArrayList<>(
			chatMessageRepository.findAllByRoomOrderByCreatedDateDesc(chatRoom, pageable).getContent());
		chatMessages.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));

		//then
		assertEquals(inputSize, addedIds.size());
		assertEquals(inputSize / pageSize, chatMessages.size());

		for (int i = 0; i < pageSize; i++) {
			final int index = i;
			ChatMessage chatMessage = chatMessages.get(i);
			assertAll(
				() -> assertEquals("test" + (inputSize - index), chatMessage.getMessage()),
				() -> assertEquals(((inputSize - index) % 2 != 0) ? user1.getId() : user2.getId(),
					chatMessage.getWriterId())
			);
		}
	}

	@Test
	void createChatMessage_success_page_2() {
		//given
		Users user1 = Users.of(OauthType.KAKAO, 1L);
		Users user2 = Users.of(OauthType.KAKAO, 2L);
		usersRepository.saveAll(List.of(user1, user2));

		int inputSize = 100;
		int pageSize = 10;

		Long chatRoomId = chatService.createChatRoom(user1.getId(), user2.getId());

		ArrayList<Long> addedIds = new ArrayList<>();
		for (int i = 1; i <= inputSize; i++) {
			Long userId = (i % 2) != 0 ? user1.getId() : user2.getId();
			Long resultId = chatService.createChatMessage(chatRoomId, userId, "test" + i);
			addedIds.add(resultId);
		}
		Collections.sort(addedIds, Collections.reverseOrder());

		//when
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		Pageable pageable = PageRequest.of(1, pageSize, Sort.by("createdDate").descending());

		ArrayList<ChatMessageDto> chatMessages = new ArrayList<>(chatService.getChatMessages(chatRoom.getId(), pageable).toList());
		chatMessages.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));

		//then
		assertEquals(inputSize, addedIds.size());
		assertEquals(inputSize / pageSize, chatMessages.size());

		for (int i = 0; i < pageSize; i++) {
			final int index = i;
			ChatMessageDto chatMessage = chatMessages.get(i);
			assertAll(
				() -> assertEquals("test" + (inputSize - index - pageSize), chatMessage.getMessage()),
				() -> assertEquals(((inputSize - index - pageSize) % 2 != 0) ? user1.getId() : user2.getId(),
					chatMessage.getWriterId())
			);
		}
	}

	@Test
	void createChatMessage_fail_user_not_found() {
		//given

		//when

		//then
		assertThrows(UserNotFoundException.class, () -> chatService.createChatMessage(1L, 1L, ""));
	}

	@Test
	void createChatMessage_fail_room_not_found() {
		//given
		Users user = Users.of(OauthType.KAKAO, 1L);
		usersRepository.save(user);

		//when

		//then
		assertThrows(ChatRoomNotFoundException.class, () -> chatService.createChatMessage(1L, user.getId(), ""));
	}
}
