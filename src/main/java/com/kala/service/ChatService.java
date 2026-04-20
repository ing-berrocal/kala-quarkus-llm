package com.kala.service;

import com.kala.api.error.ApiException;
import com.kala.api.request.CreateConversationMessageRequest;
import com.kala.model.AssitResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ChatService {

    @Inject
    AIFacebookFeedPage aiFacebookFeedPage;

    public AssitResponse sendMessage(CreateConversationMessageRequest request) {
        validateRequest(request);

        try {
            return aiFacebookFeedPage.query(request.getMemoryId(), "", request.getMessage().trim());
        } catch (RuntimeException exception) {
            throw new ApiException(Response.Status.INTERNAL_SERVER_ERROR, "chat agent unavailable");
        }
    }

    private void validateRequest(CreateConversationMessageRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request body is required");
        }

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            throw new IllegalArgumentException("message is required");
        }

        if (request.getMemoryId() == null) {
            throw new IllegalArgumentException("memory_id is required");
        }

        boolean hasUserId = request.getUserId() != null && !request.getUserId().isBlank();
        boolean hasSessionId = request.getSessionId() != null && !request.getSessionId().isBlank();

        if (!hasUserId && !hasSessionId) {
            throw new IllegalArgumentException("user_id or session_id is required");
        }
    }
}