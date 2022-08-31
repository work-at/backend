package com.workat.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.user.entity.UserActivity;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

	List<UserActivity> findByUser_Id(Long userId);

	void deleteByUser_Id(Long userId);
}
