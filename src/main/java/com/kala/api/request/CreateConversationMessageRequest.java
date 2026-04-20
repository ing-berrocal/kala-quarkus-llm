package com.kala.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateConversationMessageRequest {

    private String message;

    @JsonProperty("memory_id")
    private Integer memoryId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("session_id")
    private String sessionId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(Integer memoryId) {
        this.memoryId = memoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}