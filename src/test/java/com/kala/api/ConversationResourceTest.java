package com.kala.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kala.api.error.ApiException;
import com.kala.api.request.CreateConversationMessageRequest;
import com.kala.model.AssitResponse;
import com.kala.service.ChatService;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class ConversationResourceTest {

    @BeforeEach
    void resetServiceMock() {
        QuarkusMock.installMockForType(new StubChatService(), ChatService.class);
    }

    @Test
    void shouldSendConversationMessage() {
        StubChatService stub = new StubChatService();
        stub.sendMessageHandler = request -> buildResponse(Boolean.TRUE, request.getMessage(), "Hola, en que te puedo ayudar?");
        QuarkusMock.installMockForType(stub, ChatService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"message\":\"Hola\",\"memory_id\":1,\"user_id\":\"admin_1\"}")
        .when()
                .post("/conversations/messages")
        .then()
                .statusCode(200)
                .body("isQuestion", equalTo(true))
                .body("query", equalTo("Hola"))
                .body("response", equalTo("Hola, en que te puedo ayudar?"));
    }

    @Test
    void shouldReturnValidationErrorWhenMessageIsMissing() {
        StubChatService stub = new StubChatService();
        stub.sendMessageHandler = request -> {
            throw new IllegalArgumentException("message is required");
        };
        QuarkusMock.installMockForType(stub, ChatService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"memory_id\":1,\"user_id\":\"admin_1\"}")
        .when()
                .post("/conversations/messages")
        .then()
                .statusCode(400)
                .body("message", equalTo("message is required"));
    }

    @Test
    void shouldReturnValidationErrorWhenMemoryIdIsMissing() {
        StubChatService stub = new StubChatService();
        stub.sendMessageHandler = request -> {
            throw new IllegalArgumentException("memory_id is required");
        };
        QuarkusMock.installMockForType(stub, ChatService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"message\":\"Hola\",\"user_id\":\"admin_1\"}")
        .when()
                .post("/conversations/messages")
        .then()
                .statusCode(400)
                .body("message", equalTo("memory_id is required"));
    }

    @Test
    void shouldReturnApiErrorWhenAgentFails() {
        StubChatService stub = new StubChatService();
        stub.sendMessageHandler = request -> {
            throw new ApiException(Response.Status.INTERNAL_SERVER_ERROR, "chat agent unavailable");
        };
        QuarkusMock.installMockForType(stub, ChatService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"message\":\"Hola\",\"memory_id\":1,\"user_id\":\"admin_1\"}")
        .when()
                .post("/conversations/messages")
        .then()
                .statusCode(500)
                .body("message", equalTo("chat agent unavailable"));
    }

    private static AssitResponse buildResponse(Boolean isQuestion, String query, String responseText) {
        AssitResponse response = new AssitResponse();
        response.setIsQuestion(isQuestion);
        response.setQuery(query);
        response.setResponse(responseText);
        return response;
    }

    private static class StubChatService extends ChatService {
        private Function<CreateConversationMessageRequest, AssitResponse> sendMessageHandler = request -> null;

        @Override
        public AssitResponse sendMessage(CreateConversationMessageRequest request) {
            return sendMessageHandler.apply(request);
        }
    }
}