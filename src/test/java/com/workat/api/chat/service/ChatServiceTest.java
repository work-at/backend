package com.workat.api.chat.service;

import static com.workat.domain.chat.entity.ChatMessageSortType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
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
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;

@Import(DataJpaTestConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class ChatServiceTest {

	private ChatService chatService;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@BeforeEach
	void init() {
		this.chatService = new ChatService(usersRepository, userProfileRepository, chatRoomRepository,
			chatMessageRepository);
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
			() -> assertEquals(chatRoom.getOwner(), user1),
			() -> assertEquals(chatRoom.getOther(), user2)
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
		// usersRepository.saveAll(List.of(user1, user2));

		UserProfile user1Profile = UserProfile.builder()
			.user(user1)
			.nickname("user1")
			.imageUrl("user1")
			.position(DepartmentType.IT_ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();
		userProfileRepository.save(user1Profile);
		UserProfile user2Profile = UserProfile.builder()
			.user(user2)
			.nickname("user2")
			.imageUrl("user2")
			.position(DepartmentType.IT_ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();
		userProfileRepository.save(user2Profile);
		usersRepository.saveAll(List.of(user1, user2));

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);
		ChatMessage givenMessage1 = ChatMessage.of(user1.getId(), "test1");
		givenMessage1.assignRoom(givenChatroom);
		ChatMessage givenMessage2 = ChatMessage.of(user2.getId(), "test2");
		givenMessage2.assignRoom(givenChatroom);
		ChatMessage givenMessage3 = ChatMessage.of(user1.getId(), "test3");
		givenMessage3.assignRoom(givenChatroom);
		ChatMessage givenMessage4 = ChatMessage.of(user2.getId(), "test4");
		givenMessage4.assignRoom(givenChatroom);
		chatMessageRepository.saveAll(List.of(givenMessage1, givenMessage2, givenMessage3, givenMessage4));

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessage2.getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessage1.getId());
		chatRoomRepository.save(givenChatroom);

		//when
		ChatRoomResponse resultChatRooms = chatService.getChatRooms(user1.getId());

		//then
		assertEquals(resultChatRooms.getRooms().size(), 1);

		ChatRoomDto chatRoom = resultChatRooms.getRooms().get(0);
		assertAll(
			() -> assertEquals(chatRoom.getId(), givenChatroom.getId()),
			() -> assertEquals(chatRoom.getOtherUser().getUserId(), user2.getId()),
			() -> assertFalse(chatRoom.isAllRead())
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
