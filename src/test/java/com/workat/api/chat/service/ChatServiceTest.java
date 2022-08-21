package com.workat.api.chat.service;

import static com.workat.domain.chat.entity.ChatMessageSortType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.workat.api.chat.dto.ChatRoomDto;
import com.workat.api.chat.dto.response.ChatMessageResponse;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.ChatRoomNotFoundException;
import com.workat.common.exception.ConflictException;
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

	private List<Users> saveUsers(int size) {
		List<Users> users = IntStream.range(0, size).mapToObj(idx -> {
			Users user = Users.of(OauthType.KAKAO, (long)idx);

			UserProfile userProfile = UserProfile.builder()
				.user(user)
				.nickname("user" + idx)
				.imageUrl("user" + idx)
				.position(DepartmentType.ENGINEER)
				.workingYear(DurationType.JUNIOR)
				.build();

			userProfileRepository.save(userProfile);

			return user;
		}).collect(Collectors.toList());

		usersRepository.saveAll(users);

		return users;
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
			() -> assertEquals(chatRoom.getApplicant(), user2)
		);
	}

	@Test
	void createChatRoom_fail_user_not_found() {
		//given

		//when

		//then
		assertThrows(UserNotFoundException.class, () -> chatService.createChatRoom(1L, 2L));
	}

	@DisplayName("채팅룸은 중복해서 생성될 수 없다.")
	@Test
	void createChatRoom_fail_redundantChat() {
		// given
		List<Users> users = saveUsers(2);

		Long ownerId = users.get(0).getId();
		Long applicantId = users.get(1).getId();

		// when
		chatService.createChatRoom(ownerId, applicantId);

		// then
		assertThrows(ConflictException.class,
			() -> chatService.createChatRoom(ownerId, applicantId)
		);
	}

	@DisplayName("자기 자신과 채팅룸을 만들 수 없다.")
	@Test
	void createChatRoom_fail_self_chat() {
		// given
		List<Users> users = saveUsers(1);

		Long ownerId = users.get(0).getId();

		// when

		// then
		assertThrows(BadRequestException.class,
			() -> chatService.createChatRoom(ownerId, ownerId)
		);
	}

	@Test
	void confirmChatRooms_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		Long chatRoomId = chatService.createChatRoom(user1.getId(), user2.getId());

		//when
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		assertFalse(chatRoom.isStart());

		chatRoom.chattingConfirm(user1.getId());
		assertTrue(chatRoom.isStart());

		ChatRoomResponse response = chatService.getChatRooms(user1.getId());

		//then
		assertEquals(response.getRooms().size(), 1);
	}

	@Test
	void getChatRooms_case1_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

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
	void getChatRooms_case2_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);
		chatRoomRepository.save(givenChatroom);

		//when
		givenChatroom.deleteRoom(user1.getId());

		ChatRoomResponse resultChatRooms = chatService.getChatRooms(user1.getId());

		//then
		assertEquals(resultChatRooms.getRooms().size(), 0);
	}

	@Test
	void getChatRooms_case3_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);
		chatRoomRepository.save(givenChatroom);

		//when
		givenChatroom.deleteRoom(user2.getId());

		ChatRoomResponse resultChatRooms = chatService.getChatRooms(user1.getId());

		//then
		assertEquals(resultChatRooms.getRooms().size(), 1);

		ChatRoomDto chatRoom = resultChatRooms.getRooms().get(0);
		assertAll(
			() -> assertEquals(chatRoom.getId(), givenChatroom.getId()),
			() -> assertEquals(chatRoom.getOtherUser().getUserId(), user2.getId()),
			() -> assertFalse(chatRoom.isStart()),
			() -> assertTrue(chatRoom.isDeletedByOtherUser()),
			() -> assertTrue(chatRoom.isAllRead())
		);
	}

	@Test
	void getChatRoomMessage_case1_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

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
		ChatMessage givenMessage5 = ChatMessage.of(user1.getId(), "test5");
		givenMessage5.assignRoom(givenChatroom);
		ChatMessage givenMessage6 = ChatMessage.of(user2.getId(), "test6");
		givenMessage6.assignRoom(givenChatroom);
		ChatMessage givenMessage7 = ChatMessage.of(user1.getId(), "test7");
		givenMessage7.assignRoom(givenChatroom);
		ChatMessage givenMessage8 = ChatMessage.of(user2.getId(), "test8");
		givenMessage8.assignRoom(givenChatroom);
		chatMessageRepository.saveAll(
			List.of(givenMessage1, givenMessage2, givenMessage3, givenMessage4, givenMessage5, givenMessage6,
				givenMessage7, givenMessage8));

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessage2.getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessage1.getId());
		chatRoomRepository.save(givenChatroom);

		//when
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(givenChatroom.getId(),
			givenMessage1.getId(), AFTER);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 7);
	}

	@Test
	void getChatRoomMessage_case2_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

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
		ChatMessage givenMessage5 = ChatMessage.of(user1.getId(), "test5");
		givenMessage5.assignRoom(givenChatroom);
		ChatMessage givenMessage6 = ChatMessage.of(user2.getId(), "test6");
		givenMessage6.assignRoom(givenChatroom);
		ChatMessage givenMessage7 = ChatMessage.of(user1.getId(), "test7");
		givenMessage7.assignRoom(givenChatroom);
		ChatMessage givenMessage8 = ChatMessage.of(user2.getId(), "test8");
		givenMessage8.assignRoom(givenChatroom);
		chatMessageRepository.saveAll(
			List.of(givenMessage1, givenMessage2, givenMessage3, givenMessage4, givenMessage5, givenMessage6,
				givenMessage7, givenMessage8));

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessage2.getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessage1.getId());
		chatRoomRepository.save(givenChatroom);

		//when
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(givenChatroom.getId(),
			givenMessage7.getId(), AFTER);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 1);
	}

	@Test
	void getChatRoomMessage_case3_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

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
		ChatMessage givenMessage5 = ChatMessage.of(user1.getId(), "test5");
		givenMessage5.assignRoom(givenChatroom);
		ChatMessage givenMessage6 = ChatMessage.of(user2.getId(), "test6");
		givenMessage6.assignRoom(givenChatroom);
		ChatMessage givenMessage7 = ChatMessage.of(user1.getId(), "test7");
		givenMessage7.assignRoom(givenChatroom);
		ChatMessage givenMessage8 = ChatMessage.of(user2.getId(), "test8");
		givenMessage8.assignRoom(givenChatroom);
		chatMessageRepository.saveAll(
			List.of(givenMessage1, givenMessage2, givenMessage3, givenMessage4, givenMessage5, givenMessage6,
				givenMessage7, givenMessage8));

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessage2.getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessage1.getId());
		chatRoomRepository.save(givenChatroom);

		//when
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(givenChatroom.getId(),
			givenMessage1.getId(), BEFORE);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 0);
	}

	@Test
	void getChatRooms_success_room_is_empty() {
		//given
		List<Users> users = saveUsers(1);

		Users user = users.get(0);

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
		List<Users> users = saveUsers(1);

		Users user = users.get(0);

		//when

		//then
		assertThrows(ChatRoomNotFoundException.class, () -> chatService.createChatMessage(1L, user.getId(), ""));
	}

}
