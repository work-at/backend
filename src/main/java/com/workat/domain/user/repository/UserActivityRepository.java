package com.workat.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.user.entity.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
}
