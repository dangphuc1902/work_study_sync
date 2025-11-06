package com.WorkStudySync.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.TaskEntity;
import com.WorkStudySync.payload.request.TaskRequest;

public interface TaskServiceImp {
    
    // Tạo task mới
    TaskEntity createTask(String userEmail, TaskRequest taskRequest);
    
    // Cập nhật task
    TaskEntity updateTask(UUID taskId, String userEmail, TaskRequest taskRequest);
    
    // Xóa task
    void deleteTask(UUID taskId, String userEmail);
    
    // Lấy task theo ID
    TaskEntity getTaskById(UUID taskId, String userEmail);
    
    // Lấy tất cả tasks của user
    List<TaskEntity> getAllTasksByUser(String userEmail);
    
    // Lấy tasks theo status
    List<TaskEntity> getTasksByStatus(String userEmail, String status);
    
    // Lấy tasks theo priority
    List<TaskEntity> getTasksByPriority(String userEmail, String priority);
    
    // Lấy tasks trong khoảng thời gian
    List<TaskEntity> getTasksByDateRange(String userEmail, LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy tasks quá hạn
    List<TaskEntity> getOverdueTasks(String userEmail);
    
    // Cập nhật status của task
    TaskEntity updateTaskStatus(UUID taskId, String userEmail, String newStatus);
}