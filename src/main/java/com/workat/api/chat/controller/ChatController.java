package com.workat.api.chat.controller;

import java.net.URI;

import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.workat.api.chat.dto.response.ChatMessageResponse;
import com.workat.api.chat.dto.response.ChatRoomResponse;
import com.workat.api.chat.service.ChatService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.chat.entity.ChatMessageSortType;
import com.workat.domain.user.entity.Users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Chatting Api")
@Validated
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
	public ResponseEntity<Long> createChattingRoom(@UserValidation Users user, @RequestParam Long otherUserId) {
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
	public ResponseEntity<ChatRoomResponse> getChattingByUser(@UserValidation Users user,
		@RequestParam @Pattern(regexp = "yyyyMMddHHmmss") String lastTime) {
		return ResponseEntity.ok(chatService.getChatRooms(user.getId()));
	}

	@ApiOperation("채팅방을 나가는 api")
	@ApiResponse(code = 200, message = "success")
	@GetMapping("/api/v1/chattings/{roomId}")
	public void deleteChattingRoom(@UserValidation Users user, @PathVariable Long roomId) {
		chatService.deleteChatRoom(user.getId(), roomId);
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
	public ResponseEntity<Long> createMessage(@PathVariable long roomId, @RequestParam Long writerId,
		@RequestParam String message) {
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
		@RequestParam(required = false) Long messageId, @RequestParam ChatMessageSortType sortType) {
		return ResponseEntity.ok(
			chatService.getChatMessages(roomId, messageId == null ? Long.MAX_VALUE : messageId, sortType));
	}
}
