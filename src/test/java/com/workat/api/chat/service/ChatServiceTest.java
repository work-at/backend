package com.workat.api.chat.service;

import static com.workat.domain.chat.entity.ChatMessageSortType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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

import com.workat.api.chat.dto.ChatMessageDto;
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
import com.workat.domain.user.entity.UserBlocking;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.blocking.UserBlockingRepository;
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
	private UserBlockingRepository userBlockingRepository;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@BeforeEach
	void init() {
		this.chatService = new ChatService(usersRepository, userProfileRepository, userBlockingRepository,
			chatRoomRepository, chatMessageRepository);
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

		ChatMessage chatMessage1 = ChatMessage.of(user1.getId(), "1");
		chatMessage1.assignRoom(chatRoom);
		ChatMessage chatMessage2 = ChatMessage.of(user1.getId(), "2");
		chatMessage2.assignRoom(chatRoom);
		ChatMessage chatMessage3 = ChatMessage.of(user1.getId(), "3");
		chatMessage3.assignRoom(chatRoom);
		ChatMessage chatMessage4 = ChatMessage.of(user1.getId(), "4");
		chatMessage4.assignRoom(chatRoom);
		chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2, chatMessage3, chatMessage4));

		ChatRoomResponse response = chatService.getChatRooms(user1.getId());

		//then
		assertEquals(response.getRooms().size(), 1);
		assertEquals(response.getRooms().get(0).getLastMessage(), "4");
		assertEquals(response.getRooms().get(0).getLastMessageId(), chatMessage4.getId());
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
			() -> assertFalse(chatRoom.isAllRead()),
			() -> assertFalse(chatRoom.getOtherUser().isOwner())
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
		givenChatroom.deleteRoom(user1.getId(), 0L);

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

		ChatRoomResponse resultChatRooms = chatService.getChatRooms(user2.getId());

		//then
		assertEquals(resultChatRooms.getRooms().size(), 1);

		ChatRoomDto chatRoom = resultChatRooms.getRooms().get(0);
		assertAll(
			() -> assertEquals(chatRoom.getId(), givenChatroom.getId()),
			() -> assertEquals(chatRoom.getOtherUser().getUserId(), user1.getId()),
			() -> assertFalse(chatRoom.isStart()),
			() -> assertFalse(chatRoom.isDeletedByOtherUser()),
			() -> assertTrue(chatRoom.isAllRead()),
			() -> assertTrue(chatRoom.getOtherUser().isOwner())
		);
	}

	@Test
	void getChatRooms_case4_success() {
		//given
		Users user1 = Users.of(OauthType.KAKAO, 1L);
		Users user2 = Users.of(OauthType.KAKAO, 2L);
		Users user3 = Users.of(OauthType.KAKAO, 3L);
		Users user4 = Users.of(OauthType.KAKAO, 4L);
		Users user5 = Users.of(OauthType.KAKAO, 5L);

		UserProfile user1Profile = UserProfile.builder()
			.user(user1)
			.nickname("user1")
			.imageUrl("user1")
			.position(DepartmentType.ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();
		userProfileRepository.save(user1Profile);
		UserProfile user2Profile = UserProfile.builder()
			.user(user2)
			.nickname("user2")
			.imageUrl("user2")
			.position(DepartmentType.ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();
		userProfileRepository.save(user2Profile);
		UserProfile user3Profile = UserProfile.builder()
			.user(user3)
			.nickname("user3")
			.imageUrl("user3")
			.position(DepartmentType.ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();
		userProfileRepository.save(user3Profile);
		UserProfile user4Profile = UserProfile.builder()
			.user(user4)
			.nickname("user4")
			.imageUrl("user4")
			.position(DepartmentType.ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();
		userProfileRepository.save(user4Profile);
		UserProfile user5Profile = UserProfile.builder()
			.user(user5)
			.nickname("user5")
			.imageUrl("user5")
			.position(DepartmentType.ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();
		userProfileRepository.save(user5Profile);
		usersRepository.saveAll(List.of(user1, user2, user3, user4, user5));

		ChatRoom givenChatroom1 = ChatRoom.of();
		givenChatroom1.assignUsers(user1, user2);

		ChatRoom givenChatroom2 = ChatRoom.of();
		givenChatroom2.assignUsers(user1, user3);

		ChatRoom givenChatroom3 = ChatRoom.of();
		givenChatroom3.assignUsers(user1, user4);

		ChatRoom givenChatroom4 = ChatRoom.of();
		givenChatroom4.assignUsers(user1, user5);
		chatRoomRepository.saveAll(List.of(givenChatroom1, givenChatroom2, givenChatroom3, givenChatroom4));

		UserBlocking userBlocking1 = UserBlocking.of();
		userBlocking1.assignUsers(user1, user2);

		UserBlocking userBlocking2 = UserBlocking.of();
		userBlocking2.assignUsers(user3, user1);
		userBlockingRepository.saveAll(List.of(userBlocking1, userBlocking2));

		//when
		givenChatroom2.deleteRoom(user3.getId(), 0L);

		ChatRoomResponse resultChatRooms = chatService.getChatRooms(user1.getId());

		//then
		assertEquals(resultChatRooms.getRooms().size(), 3);

		ChatRoomDto chatRoom1 = resultChatRooms.getRooms().get(0);
		assertAll(
			() -> assertEquals(chatRoom1.getId(), givenChatroom4.getId()),
			() -> assertEquals(chatRoom1.getOtherUser().getUserId(), user5.getId()),
			() -> assertFalse(chatRoom1.isStart()),
			() -> assertFalse(chatRoom1.isBlockedByOtherUser()),
			() -> assertFalse(chatRoom1.isDeletedByOtherUser()),
			() -> assertTrue(chatRoom1.isAllRead())
		);

		ChatRoomDto chatRoom2 = resultChatRooms.getRooms().get(1);
		assertAll(
			() -> assertEquals(chatRoom2.getId(), givenChatroom3.getId()),
			() -> assertEquals(chatRoom2.getOtherUser().getUserId(), user4.getId()),
			() -> assertFalse(chatRoom2.isStart()),
			() -> assertFalse(chatRoom2.isBlockedByOtherUser()),
			() -> assertFalse(chatRoom2.isDeletedByOtherUser()),
			() -> assertTrue(chatRoom2.isAllRead())
		);

		ChatRoomDto chatRoom3 = resultChatRooms.getRooms().get(2);
		assertAll(
			() -> assertEquals(chatRoom3.getId(), givenChatroom2.getId()),
			() -> assertEquals(chatRoom3.getOtherUser().getUserId(), user3.getId()),
			() -> assertFalse(chatRoom3.isStart()),
			() -> assertTrue(chatRoom3.isBlockedByOtherUser()),
			() -> assertTrue(chatRoom3.isDeletedByOtherUser()),
			() -> assertTrue(chatRoom3.isAllRead())
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
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(user1, givenChatroom.getId(),
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
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(user1, givenChatroom.getId(),
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
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(user1, givenChatroom.getId(),
			givenMessage3.getId(), BEFORE);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 2);
	}

	@Test
	void getChatRoomMessage_case4_success() {
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
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(user1, givenChatroom.getId(),
			givenMessage3.getId(), AFTER);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 5);
	}

	@Test
	void getChatRoomMessage_case5_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);

		ArrayList<ChatMessage> givenMessages = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			ChatMessage givenMessage = ChatMessage.of(user1.getId(), "test" + i);
			givenMessage.assignRoom(givenChatroom);
			givenMessages.add(givenMessage);
		}
		chatMessageRepository.saveAll(givenMessages);

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessages.get(70).getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessages.get(60).getId());
		chatRoomRepository.save(givenChatroom);

		//when
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(user1, givenChatroom.getId(), null,
			AFTER);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 29);
	}

	@Test
	void getChatRoomMessage_case6_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);

		ArrayList<ChatMessage> givenMessages = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			ChatMessage givenMessage = ChatMessage.of(user1.getId(), "test" + i);
			givenMessage.assignRoom(givenChatroom);
			givenMessages.add(givenMessage);
		}
		chatMessageRepository.saveAll(givenMessages);

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessages.get(70).getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessages.get(60).getId());
		chatRoomRepository.save(givenChatroom);

		//when
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(user1, givenChatroom.getId(), null,
			BEFORE);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 50);
		assertEquals(resultMessageResponse.getMessages().get(0).getId(), givenMessages.get(20).getId());
	}

	@Test
	void getChatRoomMessage_case7_success() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);

		ArrayList<ChatMessage> givenMessages = new ArrayList<>();
		for (int i = 1; i <= 150; i++) {
			ChatMessage givenMessage = ChatMessage.of(user1.getId(), "test" + i);
			givenMessage.assignRoom(givenChatroom);
			givenMessages.add(givenMessage);
		}
		chatMessageRepository.saveAll(givenMessages);

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessages.get(70).getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessages.get(60).getId());
		chatRoomRepository.save(givenChatroom);

		//when
		ChatMessageResponse resultMessageResponse = chatService.getChatMessages(user1, givenChatroom.getId(), null,
			INIT);

		//then
		assertEquals(resultMessageResponse.getMessages().size(), 101);
		assertEquals(resultMessageResponse.getMessages().get(0).getId(), givenMessages.get(20).getId());
		assertEquals(resultMessageResponse.getMessages().get(100).getId(), givenMessages.get(120).getId());
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

	@Test
	void deleteChatRoom_case1() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);

		ArrayList<ChatMessage> givenMessages = new ArrayList<>();
		for (int i = 1; i <= 250; i++) {
			ChatMessage givenMessage = ChatMessage.of(user1.getId(), "test" + i);
			givenMessage.assignRoom(givenChatroom);
			givenMessages.add(givenMessage);
		}
		chatMessageRepository.saveAll(givenMessages);
		chatRoomRepository.save(givenChatroom);

		chatService.deleteChatRoom(user2.getId(), givenChatroom.getId(), givenMessages.get(249).getId());

		//when
		ChatRoomResponse response = chatService.getChatRooms(user2.getId());

		//then
		assertEquals(0, response.getRooms().size());
	}

	@Test
	void deleteChatRoom_case2() {
		//given
		List<Users> users = saveUsers(2);

		Users user1 = users.get(0);
		Users user2 = users.get(1);

		ChatRoom givenChatroom = ChatRoom.of();
		givenChatroom.assignUsers(user1, user2);

		ArrayList<ChatMessage> givenMessages = new ArrayList<>();
		for (int i = 1; i <= 250; i++) {
			ChatMessage givenMessage = ChatMessage.of(user1.getId(), "test" + i);
			givenMessage.assignRoom(givenChatroom);
			givenMessages.add(givenMessage);
		}
		chatMessageRepository.saveAll(givenMessages);

		givenChatroom.setUsersLastCheckingMessageId(user1.getId(), givenMessages.get(0).getId());
		givenChatroom.setUsersLastCheckingMessageId(user2.getId(), givenMessages.get(99).getId());
		chatRoomRepository.save(givenChatroom);

		chatService.deleteChatRoom(user2.getId(), givenChatroom.getId(), givenChatroom.getApplicantLastCheckingMessageId());

		chatService.createChatRoom(user1.getId(), user2.getId());

		//when
		ChatRoomResponse response1 = chatService.getChatRooms(user2.getId());
		ChatMessageResponse response2 = chatService.getChatMessages(user2, givenChatroom.getId(), null, INIT);

		assertEquals(1, response1.getRooms().size());
		assertEquals(50, response2.getMessages().size());
		assertEquals(givenMessages.get(149).getId(), response2.getMessages().get(49).getId());

		chatService.postRoomLastUserCheckingMessage(user2.getId(), givenChatroom.getId(), response2.getMessages().get(49).getId());
		ChatMessageResponse response3 = chatService.getChatMessages(user2, givenChatroom.getId(), null, AFTER);

		assertEquals(50, response3.getMessages().size());
		assertEquals(givenMessages.get(199).getId(), response3.getMessages().get(49).getId());

		chatService.postRoomLastUserCheckingMessage(user2.getId(), givenChatroom.getId(), response3.getMessages().get(49).getId());
		ChatMessageResponse response4 = chatService.getChatMessages(user2, givenChatroom.getId(), null, AFTER);

		assertEquals(50, response4.getMessages().size());
		assertEquals(givenMessages.get(249).getId(), response4.getMessages().get(49).getId());
	}

}
