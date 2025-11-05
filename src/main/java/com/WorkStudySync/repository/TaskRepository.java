package com.WorkStudySync.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.TaskEntity;
import com.WorkStudySync.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    
    // Tìm tasks theo user
    List<TaskEntity> findByUser(UserEntity user);
    
    // Tìm tasks theo user và status
    List<TaskEntity> findByUserAndStatus(UserEntity user, String status);
    
    // Tìm tasks theo user và priority
    List<TaskEntity> findByUserAndPriority(UserEntity user, String priority);
    
    // Tìm tasks có deadline trong khoảng thời gian
    @Query("SELECT t FROM TaskEntity t WHERE t.user = :user AND t.dueDate BETWEEN :startDate AND :endDate")
    List<TaskEntity> findByUserAndDueDateBetween(
        @Param("user") UserEntity user, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    // Tìm tasks quá hạn
    @Query("SELECT t FROM TaskEntity t WHERE t.user = :user AND t.dueDate < :currentDate AND t.status != 'completed'")
    List<TaskEntity> findOverdueTasks(@Param("user") UserEntity user, @Param("currentDate") LocalDateTime currentDate);
    
    // Đếm số tasks theo status
    @Query("SELECT COUNT(t) FROM TaskEntity t WHERE t.user = :user AND t.status = :status")
    Long countByUserAndStatus(@Param("user") UserEntity user, @Param("status") String status);
}