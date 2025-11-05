package com.WorkStudySync.repository;

import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.CommentEntity;
import com.WorkStudySync.entity.CommunityPostEntity;
import com.WorkStudySync.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    
    // Tìm comments theo post
    @Query("SELECT c FROM CommentEntity c WHERE c.post = :post ORDER BY c.createdAt ASC")
    List<CommentEntity> findByPost(@Param("post") CommunityPostEntity post);
    
    // Tìm comments theo user
    List<CommentEntity> findByUser(UserEntity user);
    
    // Đếm số lượng comments của một post
    Long countByPost(CommunityPostEntity post);
    
    // Đếm số lượng comments của user
    Long countByUser(UserEntity user);
}