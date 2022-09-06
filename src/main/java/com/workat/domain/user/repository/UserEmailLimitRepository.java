package com.workat.domain.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.workat.domain.user.entity.UserEmailLimit;

public interface UserEmailLimitRepository extends CrudRepository<UserEmailLimit, Long> {
}
