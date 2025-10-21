package com.WorkStudySync.entity;
import java.util.UUID;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserRoleEntityId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UUID roleId;

    // Default constructor
    public UserRoleEntityId() {}

    public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}

	// Parameterized constructor
    public UserRoleEntityId(UUID userId, UUID roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleEntityId that = (UserRoleEntityId) o;
        return userId.equals(that.userId) && roleId.equals(that.roleId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(userId, roleId);
    }
}