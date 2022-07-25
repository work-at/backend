package com.workat.api.chat.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.workat.api.chat.dto.response.ChatMessageResponse;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.api.chat.service.ChatService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Chatting Api")
@RequiredArgsConstructor
@RestController
public class ChatController {

	private final ChatService chatService;

	@ApiOperation("채팅방을 생성하는 api")
	@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "otherUserId", value = "1", required = true, dataType = "long", example = "1"),
	})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success", response = Long.class)
	})
	@PostMapping("/api/v1/chattings")
	public ResponseEntity<Long> createChatting(@UserValidation Users user, Long otherUserId) {
		Long id = chatService.createChatRoom(user.getId(), otherUserId);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(uri).build();
	}

	@ApiOperation("채팅방을 가져오는 api")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success", response = ChatRoomResponse.class)
	})
	@GetMapping("/api/v1/users/chattings")
	public ResponseEntity<ChatRoomResponse> getChattingByUser(@UserValidation Users user) {
		return ResponseEntity.ok(chatService.getChatRooms(user.getId()));
	}

	@ApiOperation("채팅 메세지를 생성하는 api")
	@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "writerId", value = "1", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "message", value = "샘플 메세지 입니다.", required = true, dataType = "string", example = "샘플 메세지 입니다."),
	})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success", response = Long.class)
	})
	@PostMapping("/api/v1/chattings/{roomId}")
	public ResponseEntity<Long> createMessage(@PathVariable long roomId, Long writerId, String message) {
		Long id = chatService.createChatMessage(roomId, writerId, message);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(uri).build();
	}

	@ApiOperation("채팅 메세지를 가져오는 api")
	@ApiImplicitParams(value = {})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success", response = ChatMessageResponse.class)
	})
	@GetMapping("/api/v1/chattings/{roomId}/messages")
	public ResponseEntity<ChatMessageResponse> getMessageByRoom(@PathVariable Long roomId,
		@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) PageRequest pageRequest) {
		return ResponseEntity.ok(chatService.getChatMessages(roomId, pageRequest));
	}
}
