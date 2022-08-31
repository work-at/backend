package com.workat.domain.chat.repository.room;

import static com.workat.domain.chat.entity.QChatRoom.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.chat.entity.ChatRoom;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<ChatRoom> findByUserIds(Long owner, Long applicant) {
		return Optional.ofNullable(queryFactory.selectFrom(chatRoom)
			.where(usersChattingRooms(owner, applicant))
			.fetchOne());
	}

	@Override
	public List<ChatRoom> findAllByUser(Users user) {
		return queryFactory.selectFrom(chatRoom)
			.where(chatRoom.owner.eq(user).or(chatRoom.applicant.eq(user)))
			.orderBy(chatRoom.messageUpdatedTime.desc())
			.fetch();
	}

	private BooleanExpression usersChattingRooms(Long userId1, Long userId2) {
		return (chatRoom.owner.id.eq(userId1).and(chatRoom.applicant.id.eq(userId2)))
			.or(chatRoom.owner.id.eq(userId2).and(chatRoom.applicant.id.eq(userId1)));
	}
}
