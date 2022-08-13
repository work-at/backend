package com.workat.domain.chat.entity;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.workat.domain.BaseEntity;
import com.workat.domain.user.entity.Users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatRoom extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "owner_id")
	@ManyToOne
	private Users owner;

	@JoinColumn(name = "other_id")
	@ManyToOne
	private Users other;

	private Long ownerLastCheckingMessageId;

	private Long otherLastCheckingMessageId;

	private boolean isDeleted;

	public static ChatRoom of() {
		return new ChatRoom();
	}

	public void assignUsers(Users owner, Users other) {
		this.owner = owner;
		this.other = other;
	}

	public void setUsersLastCheckingMessageId(Long userId, Long lastMessageId) {
		if (userId.equals(owner.getId())) {
			this.ownerLastCheckingMessageId = lastMessageId;
		} else if (userId.equals(other.getId())) {
			this.otherLastCheckingMessageId = lastMessageId;
		} else {
			throw new InvalidParameterException("this room not contains user, userId = " + userId);
		}
	}

	public Long getUsersLastCheckingMessageId(Long userId) {
		if (userId.equals(owner.getId())) {
			return this.ownerLastCheckingMessageId != null ? this.ownerLastCheckingMessageId : 0;
		} else if(userId.equals(other.getId())) {
			return this.otherLastCheckingMessageId != null ? this.otherLastCheckingMessageId : 0;
		} else {
			throw new InvalidParameterException("this room not contains user, userId = " + userId);
		}
	}

	public Long getAnotherOwnerUserId(Long userId) {
		Long ownerUserId = owner.getId();
		Long otherUserId = other.getId();

		if (ownerUserId.equals(userId)) {
			return otherUserId;
		} else if (otherUserId.equals(userId)) {
			return ownerUserId;
		} else {
			throw new InvalidParameterException("this room not contain userId = " + userId);
		}
	}

	public void deleteRoom() {
		this.isDeleted = true;
	}
}
