package com.WorkStudySync.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.ScheduleEntity;
import com.WorkStudySync.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, UUID> {
    
    // Tìm schedules theo user
    List<ScheduleEntity> findByUser(UserEntity user);
    
    // Tìm schedules theo user và reminder
    List<ScheduleEntity> findByUserAndReminder(UserEntity user, Boolean reminder);
    
    // Tìm schedules trong khoảng thời gian
    @Query("SELECT s FROM ScheduleEntity s WHERE s.user = :user AND s.startTime BETWEEN :startDate AND :endDate ORDER BY s.startTime ASC")
    List<ScheduleEntity> findByUserAndStartTimeBetween(
        @Param("user") UserEntity user, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    // Tìm schedules trong ngày hôm nay
    @Query("SELECT s FROM ScheduleEntity s WHERE s.user = :user AND DATE(s.startTime) = DATE(:today) ORDER BY s.startTime ASC")
    List<ScheduleEntity> findTodaySchedules(@Param("user") UserEntity user, @Param("today") LocalDateTime today);
    
    // Tìm schedules sắp tới (upcoming)
    @Query("SELECT s FROM ScheduleEntity s WHERE s.user = :user AND s.startTime > :currentTime ORDER BY s.startTime ASC")
    List<ScheduleEntity> findUpcomingSchedules(@Param("user") UserEntity user, @Param("currentTime") LocalDateTime currentTime);
}