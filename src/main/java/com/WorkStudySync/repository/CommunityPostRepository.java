package com.WorkStudySync.repository;

import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.CommunityPostEntity;
import com.WorkStudySync.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, UUID> {
    
    // Tìm posts theo user
    List<CommunityPostEntity> findByUser(UserEntity user);
    
    // Tìm tất cả posts (sắp xếp theo thời gian tạo mới nhất)
    @Query("SELECT p FROM CommunityPostEntity p ORDER BY p.createdAt DESC")
    List<CommunityPostEntity> findAllOrderByCreatedAtDesc();
    
    // Tìm posts theo keyword trong title hoặc content
    @Query("SELECT p FROM CommunityPostEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY p.createdAt DESC")
    List<CommunityPostEntity> searchPosts(@Param("keyword") String keyword);
    
    // Tìm posts có liên kết với note
    @Query("SELECT p FROM CommunityPostEntity p WHERE p.note IS NOT NULL ORDER BY p.createdAt DESC")
    List<CommunityPostEntity> findPostsWithNotes();
    
    // Đếm số lượng posts của user
    Long countByUser(UserEntity user);
}