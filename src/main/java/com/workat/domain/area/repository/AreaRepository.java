package com.workat.domain.area.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.area.entity.Area;

public interface AreaRepository extends JpaRepository<Area, String> {
}
