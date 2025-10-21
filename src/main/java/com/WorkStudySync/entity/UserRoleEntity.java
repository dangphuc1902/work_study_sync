package com.WorkStudySync.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRoleEntity {

    @EmbeddedId
    private UserRoleEntityId id;

    @ManyToOne
    @MapsId("userId") // trỏ đến field userId trong UserRoleEntityId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("roleId") // trỏ đến field roleId trong UserRoleEntityId
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    // Getters and setters
    public UserRoleEntityId getId() {
        return id;
    }

    public void setId(UserRoleEntityId id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}
