package com.workat.domain.chat.entity;

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

	private boolean isDeleted;

	public static ChatRoom of() {
		return new ChatRoom();
	}

	public void assignUsers(Users owner, Users other) {
		this.owner = owner;
		this.other = other;
	}

	public void deleteRoom() {
		this.isDeleted = true;
	}
}
