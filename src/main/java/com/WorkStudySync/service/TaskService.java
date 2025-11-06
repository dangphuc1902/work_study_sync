package com.WorkStudySync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.TaskEntity;
import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.payload.request.TaskRequest;
import com.WorkStudySync.repository.TaskRepository;
import com.WorkStudySync.repository.UserRepository;
import com.WorkStudySync.service.imp.TaskServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService implements TaskServiceImp {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskEntity createTask(String userEmail, TaskRequest taskRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskEntity task = new TaskEntity();
        task.setUser(user);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setPriority(taskRequest.getPriority() != null ? taskRequest.getPriority() : "low");
        task.setStatus(taskRequest.getStatus() != null ? taskRequest.getStatus() : "pending");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Override
    public TaskEntity updateTask(UUID taskId, String userEmail, TaskRequest taskRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Kiểm tra quyền sở hữu
        if (!task.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to update this task");
        }

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        if (taskRequest.getPriority() != null) {
            task.setPriority(taskRequest.getPriority());
        }
        if (taskRequest.getStatus() != null) {
            task.setStatus(taskRequest.getStatus());
        }
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID taskId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Kiểm tra quyền sở hữu
        if (!task.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to delete this task");
        }

        taskRepository.delete(task);
    }

    @Override
    public TaskEntity getTaskById(UUID taskId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Kiểm tra quyền sở hữu
        if (!task.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to view this task");
        }

        return task;
    }

    @Override
    public List<TaskEntity> getAllTasksByUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUser(user);
    }

    @Override
    public List<TaskEntity> getTasksByStatus(String userEmail, String status) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUserAndStatus(user, status);
    }

    @Override
    public List<TaskEntity> getTasksByPriority(String userEmail, String priority) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUserAndPriority(user, priority);
    }

    @Override
    public List<TaskEntity> getTasksByDateRange(String userEmail, LocalDateTime startDate, LocalDateTime endDate) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUserAndDueDateBetween(user, startDate, endDate);
    }

    @Override
    public List<TaskEntity> getOverdueTasks(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findOverdueTasks(user, LocalDateTime.now());
    }

    @Override
    public TaskEntity updateTaskStatus(UUID taskId, String userEmail, String newStatus) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Kiểm tra quyền sở hữu
        if (!task.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to update this task");
        }

        task.setStatus(newStatus);
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }
}