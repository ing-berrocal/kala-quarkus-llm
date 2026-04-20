package com.kala.api;

import com.kala.api.request.CreateConversationMessageRequest;
import com.kala.model.AssitResponse;
import com.kala.service.ChatService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/conversations/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConversationResource {

    @Inject
    ChatService chatService;

    @POST
    public AssitResponse sendMessage(CreateConversationMessageRequest request) {
        // Security placeholder: restrict to ADMIN/SUPPORT once auth is enabled.
        return chatService.sendMessage(request);
    }
}