package com.workat.domain.chat.entity;

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
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatRoom extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "user1_id")
	@ManyToOne
	private Users user1;

	@JoinColumn(name = "user2_id")
	@ManyToOne
	private Users user2;

	public static ChatRoom of() {
		return new ChatRoom();
	}

	public void assignUsers(Users user1, Users user2) {
		this.user1 = user1;
		this.user2 = user2;
	}
}
