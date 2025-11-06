package com.WorkStudySync.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.WorkStudySync.entity.TaskEntity;
import com.WorkStudySync.payload.request.TaskRequest;
import com.WorkStudySync.payload.response.BaseResponse;
import com.WorkStudySync.service.imp.TaskServiceImp;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskServiceImp taskService;

    // Helper method để lấy email từ Security Context
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            TaskEntity task = taskService.createTask(userEmail, taskRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(201);
            response.setMessage("Task created successfully");
            response.setData(task);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        try {
            String userEmail = getCurrentUserEmail();
            List<TaskEntity> tasks = taskService.getAllTasksByUser(userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Tasks retrieved successfully");
            response.setData(tasks);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable UUID taskId) {
        try {
            String userEmail = getCurrentUserEmail();
            TaskEntity task = taskService.getTaskById(taskId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Task retrieved successfully");
            response.setData(task);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable UUID taskId, @Valid @RequestBody TaskRequest taskRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            TaskEntity task = taskService.updateTask(taskId, userEmail, taskRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Task updated successfully");
            response.setData(task);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID taskId) {
        try {
            String userEmail = getCurrentUserEmail();
            taskService.deleteTask(taskId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Task deleted successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable String status) {
        try {
            String userEmail = getCurrentUserEmail();
            List<TaskEntity> tasks = taskService.getTasksByStatus(userEmail, status);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Tasks retrieved successfully");
            response.setData(tasks);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<?> getTasksByPriority(@PathVariable String priority) {
        try {
            String userEmail = getCurrentUserEmail();
            List<TaskEntity> tasks = taskService.getTasksByPriority(userEmail, priority);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Tasks retrieved successfully");
            response.setData(tasks);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueTasks() {
        try {
            String userEmail = getCurrentUserEmail();
            List<TaskEntity> tasks = taskService.getOverdueTasks(userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Overdue tasks retrieved successfully");
            response.setData(tasks);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<?> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String userEmail = getCurrentUserEmail();
            List<TaskEntity> tasks = taskService.getTasksByDateRange(userEmail, startDate, endDate);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Tasks retrieved successfully");
            response.setData(tasks);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable UUID taskId, @RequestBody Map<String, String> statusUpdate) {
        try {
            String userEmail = getCurrentUserEmail();
            String newStatus = statusUpdate.get("status");
            TaskEntity task = taskService.updateTaskStatus(taskId, userEmail, newStatus);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Task status updated successfully");
            response.setData(task);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}