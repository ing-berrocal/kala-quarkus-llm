package com.kala.model;

public enum PostStatus {
    ACTIVE,
    INACTIVE;

    public static PostStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("status is required");
        }

        try {
            return PostStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("invalid status. allowed values: ACTIVE, INACTIVE");
        }
    }
}
