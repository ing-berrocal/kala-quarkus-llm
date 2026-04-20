package com.kala.model;

public enum CommentStatus {
    PENDING,
    PROCESSED,
    APROVED;

    public static CommentStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("status is required");
        }

        try {
            return CommentStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("invalid status. allowed values: PENDING, PROCESSED, APROVED");
        }
    }
}
