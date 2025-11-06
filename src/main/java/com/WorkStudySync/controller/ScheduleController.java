package com.WorkStudySync.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.ScheduleEntity;
import com.WorkStudySync.payload.request.ScheduleRequest;
import com.WorkStudySync.payload.response.BaseResponse;
import com.WorkStudySync.service.imp.ScheduleServiceImp;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleServiceImp scheduleService;

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            ScheduleEntity schedule = scheduleService.createSchedule(userEmail, scheduleRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(201);
            response.setMessage("Schedule created successfully");
            response.setData(schedule);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSchedules() {
        try {
            String userEmail = getCurrentUserEmail();
            List<ScheduleEntity> schedules = scheduleService.getAllSchedulesByUser(userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Schedules retrieved successfully");
            response.setData(schedules);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getScheduleById(@PathVariable UUID scheduleId) {
        try {
            String userEmail = getCurrentUserEmail();
            ScheduleEntity schedule = scheduleService.getScheduleById(scheduleId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Schedule retrieved successfully");
            response.setData(schedule);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable UUID scheduleId, @Valid @RequestBody ScheduleRequest scheduleRequest) {
        try {
            String userEmail = getCurrentUserEmail();
            ScheduleEntity schedule = scheduleService.updateSchedule(scheduleId, userEmail, scheduleRequest);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Schedule updated successfully");
            response.setData(schedule);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable UUID scheduleId) {
        try {
            String userEmail = getCurrentUserEmail();
            scheduleService.deleteSchedule(scheduleId, userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Schedule deleted successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodaySchedules() {
        try {
            String userEmail = getCurrentUserEmail();
            List<ScheduleEntity> schedules = scheduleService.getTodaySchedules(userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Today's schedules retrieved successfully");
            response.setData(schedules);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingSchedules() {
        try {
            String userEmail = getCurrentUserEmail();
            List<ScheduleEntity> schedules = scheduleService.getUpcomingSchedules(userEmail);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Upcoming schedules retrieved successfully");
            response.setData(schedules);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<?> getSchedulesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String userEmail = getCurrentUserEmail();
            List<ScheduleEntity> schedules = scheduleService.getSchedulesByDateRange(userEmail, startDate, endDate);
            
            BaseResponse response = new BaseResponse();
            response.setStatusCode(200);
            response.setMessage("Schedules retrieved successfully");
            response.setData(schedules);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse response = new BaseResponse();
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}