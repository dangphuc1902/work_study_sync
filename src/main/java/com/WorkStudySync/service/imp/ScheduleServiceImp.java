package com.WorkStudySync.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.ScheduleEntity;
import com.WorkStudySync.payload.request.ScheduleRequest;

public interface ScheduleServiceImp {
    
    // Tạo schedule mới
    ScheduleEntity createSchedule(String userEmail, ScheduleRequest scheduleRequest);
    
    // Cập nhật schedule
    ScheduleEntity updateSchedule(UUID scheduleId, String userEmail, ScheduleRequest scheduleRequest);
    
    // Xóa schedule
    void deleteSchedule(UUID scheduleId, String userEmail);
    
    // Lấy schedule theo ID
    ScheduleEntity getScheduleById(UUID scheduleId, String userEmail);
    
    // Lấy tất cả schedules của user
    List<ScheduleEntity> getAllSchedulesByUser(String userEmail);
    
    // Lấy schedules trong khoảng thời gian
    List<ScheduleEntity> getSchedulesByDateRange(String userEmail, LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy schedules hôm nay
    List<ScheduleEntity> getTodaySchedules(String userEmail);
    
    // Lấy schedules sắp tới
    List<ScheduleEntity> getUpcomingSchedules(String userEmail);
}