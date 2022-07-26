package com.workat.domain.chat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.workat.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@Getter
@Entity
public class ChatMessage extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "chat_room_id")
	@ManyToOne
	private ChatRoom room;

	private Long writerId;

	private String message;

	public static ChatMessage of(Long writerId, String message) {
		ChatMessage chatMessage = new ChatMessage(null, null, writerId, message);
		return chatMessage;
	}

	public void assignRoom(ChatRoom room) {
		this.room = room;
	}

}
