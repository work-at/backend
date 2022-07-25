package com.workat.api.chat.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.workat.api.chat.dto.ChatMessageDto;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.api.chat.service.ChatService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/api/v1/chattings")
	public ResponseEntity<Long> createChatting(@UserValidation Users user1, @UserValidation Users user2) {
		Long id = chatService.createChatRoom(user1.getId(), user2.getId());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/api/v1/users/chattings")
	public ResponseEntity<ChatRoomResponse> getChattingByUser(@UserValidation Users user) {
		return ResponseEntity.ok(chatService.getChatRooms(user.getId()));
	}

	@PostMapping("/api/v1/chattings/{roomId}")
	public ResponseEntity<Long> createMessage(@PathVariable long roomId, @UserValidation Users writer, String message) {
		Long id = chatService.createChatMessage(roomId, writer.getId(), message);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/api/v1/chattings/{roomId}/messages")
	public ResponseEntity<Page<ChatMessageDto>> getMessageByRoom(@PathVariable Long roomId,
		@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(chatService.getChatMessages(roomId, pageable));
	}
}
