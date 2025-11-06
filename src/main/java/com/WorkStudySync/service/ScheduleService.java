package com.WorkStudySync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.WorkStudySync.entity.ScheduleEntity;
import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.payload.request.ScheduleRequest;
import com.WorkStudySync.repository.ScheduleRepository;
import com.WorkStudySync.repository.UserRepository;
import com.WorkStudySync.service.imp.ScheduleServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService implements ScheduleServiceImp {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ScheduleEntity createSchedule(String userEmail, ScheduleRequest scheduleRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setUser(user);
        schedule.setTitle(scheduleRequest.getTitle());
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        schedule.setDescription(scheduleRequest.getDescription());
        schedule.setReminder(scheduleRequest.getReminder() != null ? scheduleRequest.getReminder() : false);
        schedule.setCreatedAt(LocalDateTime.now());

        return scheduleRepository.save(schedule);
    }

    @Override
    public ScheduleEntity updateSchedule(UUID scheduleId, String userEmail, ScheduleRequest scheduleRequest) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to update this schedule");
        }

        schedule.setTitle(scheduleRequest.getTitle());
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        schedule.setDescription(scheduleRequest.getDescription());
        if (scheduleRequest.getReminder() != null) {
            schedule.setReminder(scheduleRequest.getReminder());
        }

        return scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(UUID scheduleId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to delete this schedule");
        }

        scheduleRepository.delete(schedule);
    }

    @Override
    public ScheduleEntity getScheduleById(UUID scheduleId, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You don't have permission to view this schedule");
        }

        return schedule;
    }

    @Override
    public List<ScheduleEntity> getAllSchedulesByUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return scheduleRepository.findByUser(user);
    }

    @Override
    public List<ScheduleEntity> getSchedulesByDateRange(String userEmail, LocalDateTime startDate, LocalDateTime endDate) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return scheduleRepository.findByUserAndStartTimeBetween(user, startDate, endDate);
    }

    @Override
    public List<ScheduleEntity> getTodaySchedules(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return scheduleRepository.findTodaySchedules(user, LocalDateTime.now());
    }

    @Override
    public List<ScheduleEntity> getUpcomingSchedules(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return scheduleRepository.findUpcomingSchedules(user, LocalDateTime.now());
    }
}