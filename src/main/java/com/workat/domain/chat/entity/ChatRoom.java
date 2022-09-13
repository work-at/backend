package com.workat.domain.chat.entity;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

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

	@JoinColumn(name = "applicant_id")
	@ManyToOne
	private Users applicant;

	private Long ownerLastCheckingMessageId;

	private Long applicantLastCheckingMessageId;

	private Long ownerLastDeletedMessageId;

	private Long applicantLastDeletedMessageId;

	private boolean isFirstCreated;

	private boolean isStart;

	private boolean isOwnerDeleted;

	private boolean isApplicantDeleted;

	private LocalDateTime messageUpdatedTime = LocalDateTime.now();

	public static ChatRoom of() {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.isFirstCreated = true;
		chatRoom.ownerLastCheckingMessageId = -1L;
		chatRoom.applicantLastCheckingMessageId = 0L;
		chatRoom.ownerLastDeletedMessageId = -1L;
		chatRoom.applicantLastDeletedMessageId = 0L;
		return chatRoom;
	}

	public void assignUsers(Users owner, Users applicant) {
		this.owner = owner;
		this.applicant = applicant;
		this.isFirstCreated = false;
	}

	public void chattingConfirm(Long userId) {
		if (userId.equals(owner.getId())) {
			this.isStart = true;
		} else {
			throw new InvalidParameterException("this user(id : " + userId + ") no authority, wrong value");
		}
	}

	public void setMessageUpdatedTime() {
		this.messageUpdatedTime = LocalDateTime.now();
	}

	// TODO: 2022/08/17 추후 owner, applicant 구분하는 로직 람다를 이용해서 리팩터링할 예정
	public void setUsersLastCheckingMessageId(Long userId, Long lastMessageId) {
		if (userId.equals(owner.getId())) {
			this.ownerLastCheckingMessageId = lastMessageId;
		} else if (userId.equals(applicant.getId())) {
			this.applicantLastCheckingMessageId = lastMessageId;
		} else {
			throw new InvalidParameterException("this room not contains user, userId = " + userId);
		}
	}

	public Long getUsersLastCheckingMessageId(Long userId) {
		if (userId.equals(owner.getId())) {
			return this.ownerLastCheckingMessageId != null ? this.ownerLastCheckingMessageId : 0;
		} else if (userId.equals(applicant.getId())) {
			return this.applicantLastCheckingMessageId != null ? this.applicantLastCheckingMessageId : 0;
		} else {
			throw new InvalidParameterException("this room not contains user, userId = " + userId);
		}
	}

	public Long getUsersLastDeletedMessageId(Long userId) {
		if (userId.equals(owner.getId())) {
			return this.ownerLastDeletedMessageId != null ? this.ownerLastDeletedMessageId : 0;
		} else if (userId.equals(applicant.getId())) {
			return this.applicantLastDeletedMessageId != null ? this.applicantLastDeletedMessageId : 0;
		} else {
			throw new InvalidParameterException("this room not contains user, userId = " + userId);
		}
	}

	public Long getAnotherOwnerUserId(Long userId) {
		Long ownerUserId = owner.getId();
		Long applyUserId = applicant.getId();

		if (ownerUserId.equals(userId)) {
			return applyUserId;
		} else if (applyUserId.equals(userId)) {
			return ownerUserId;
		} else {
			throw new InvalidParameterException("this room not contain userId = " + userId);
		}
	}

	public void deleteRoom(Long userId, Long lastMessageId) {
		Long ownerUserId = owner.getId();
		Long applyUserId = applicant.getId();
		if (userId.equals(ownerUserId)) {
			this.isOwnerDeleted = true;
			this.ownerLastCheckingMessageId = lastMessageId;
			this.ownerLastDeletedMessageId = lastMessageId;
		} else if (userId.equals(applyUserId)) {
			this.isApplicantDeleted = true;
			this.applicantLastCheckingMessageId = lastMessageId;
			this.applicantLastDeletedMessageId = lastMessageId;
		} else {
			throw new InvalidParameterException("this room not contain userId = " + userId);
		}
	}

	public void revivalRoom(Long userId) {
		Long ownerUserId = owner.getId();
		Long applyUserId = applicant.getId();
		if (userId.equals(ownerUserId)) {
			this.isOwnerDeleted = false;
		} else if (userId.equals(applyUserId)) {
			this.isApplicantDeleted = false;
		} else {
			throw new InvalidParameterException("this room not contain userId = " + userId);
		}
	}

	public boolean isDeletedByMe(Long userId) {
		Long ownerUserId = owner.getId();
		Long applyUserId = applicant.getId();
		if (userId.equals(ownerUserId)) {
			return this.isOwnerDeleted;
		} else if (userId.equals(applyUserId)) {
			return this.isApplicantDeleted;
		} else {
			throw new InvalidParameterException("this room not contain userId = " + userId);
		}
	}

	public boolean isDeletedByOther(Long userId) {
		Long ownerUserId = owner.getId();
		Long applyUserId = applicant.getId();
		if (userId.equals(ownerUserId)) {
			return this.isApplicantDeleted;
		} else if (userId.equals(applyUserId)) {
			return this.isOwnerDeleted;
		} else {
			throw new InvalidParameterException("this room not contain userId = " + userId);
		}
	}

	public void setOthersDeleted(Long userId) {
		Long ownerUserId = owner.getId();
		Long applyUserId = applicant.getId();
		if (userId.equals(ownerUserId)) {
			this.isApplicantDeleted = false;
		} else if (userId.equals(applyUserId)) {
			this.isOwnerDeleted = false;
		} else {
			throw new InvalidParameterException("this room not contain userId = " + userId);
		}
	}

	public boolean isOwner(Long userId) {
		return owner.getId().equals(userId);
	}
}
