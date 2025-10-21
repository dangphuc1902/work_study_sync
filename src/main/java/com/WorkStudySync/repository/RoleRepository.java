package com.WorkStudySync.repository;

import java.util.Optional;
import java.util.UUID;

import com.WorkStudySync.entity.RoleEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
	// Thêm các phương thức truy vấn tùy chỉnh nếu cần
	// Ví dụ: tìm kiếm theo tên vai trò
    Optional<RoleEntity> findByName(String name);
}
