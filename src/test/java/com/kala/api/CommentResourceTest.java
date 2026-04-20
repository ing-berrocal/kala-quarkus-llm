package com.kala.api;

import java.util.List;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kala.api.request.UpdateCommentStatusRequest;
import com.kala.model.Comment;
import com.kala.model.CommentStatus;
import com.kala.service.CommentService;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@QuarkusTest
class CommentResourceTest {

    @BeforeEach
    void resetServiceMock() {
        QuarkusMock.installMockForType(new StubCommentService(), CommentService.class);
    }

    @Test
    void shouldListComments() {
        StubCommentService stub = new StubCommentService();
        stub.listHandler = status -> List.of(
                buildComment("c_1", "p_1", "26073109655722298", "Daniel Berrocal", "Informacion sobre esto", 1775070355L,
                        null, CommentStatus.PROCESSED),
                buildComment("c_2", "p_2", "26073109655722299", "Ana Perez", "Me interesa", 1775070300L,
                        "Te escribimos por inbox", CommentStatus.PENDING));
        QuarkusMock.installMockForType(stub, CommentService.class);

        given()
                .queryParam("status", "PENDING")
        .when()
                .get("/comments")
        .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].comment_id", equalTo("c_1"))
                .body("[0].comment.from.id", equalTo("26073109655722298"))
                .body("[0].status", equalTo("PROCESSED"));
    }

    @Test
    void shouldReturnValidationErrorWhenStatusIsInvalid() {
        StubCommentService stub = new StubCommentService();
        stub.listHandler = status -> {
                        throw new IllegalArgumentException("invalid status. allowed values: PENDING, PROCESSED, APROVED");
        };
        QuarkusMock.installMockForType(stub, CommentService.class);

        given()
                .queryParam("status", "INVALID")
        .when()
                .get("/comments")
        .then()
                .statusCode(400)
                .body("message", equalTo("invalid status. allowed values: PENDING, PROCESSED, APROVED"));
    }

    @Test
    void shouldUpdateCommentStatus() {
        StubCommentService stub = new StubCommentService();
        stub.updateStatusHandler = request -> buildComment(
                request.getCommentId(),
                "p_1",
                "26073109655722298",
                "Daniel Berrocal",
                "Informacion sobre esto",
                1775070355L,
                null,
                CommentStatus.APROVED);
        QuarkusMock.installMockForType(stub, CommentService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"comment_id\":\"c_1\",\"status\":\"APROVED\"}")
        .when()
                .post("/comments/status")
        .then()
                .statusCode(200)
                .body("comment_id", equalTo("c_1"))
                .body("status", equalTo("APROVED"));
    }

    @Test
    void shouldReturnValidationErrorWhenUpdateStatusRequestIsInvalid() {
        StubCommentService stub = new StubCommentService();
        stub.updateStatusHandler = request -> {
            throw new IllegalArgumentException("comment_id is required");
        };
        QuarkusMock.installMockForType(stub, CommentService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"status\":\"APROVED\"}")
        .when()
                .post("/comments/status")
        .then()
                .statusCode(400)
                .body("message", equalTo("comment_id is required"));
    }

    private static Comment buildComment(String commentId, String postId, String fromId, String fromName, String message,
            long createdTime, String responseMessage, CommentStatus status) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setPostId(postId);

        Comment.CommentFrom from = new Comment.CommentFrom();
        from.setId(fromId);
        from.setName(fromName);

        Comment.CommentDetail commentDetail = new Comment.CommentDetail();
        commentDetail.setFrom(from);
        commentDetail.setMessage(message);
        commentDetail.setCreatedTime(createdTime);
        comment.setComment(commentDetail);

        Comment.CommentResponse response = new Comment.CommentResponse();
        response.setMessage(responseMessage);
        comment.setResponse(response);

        comment.setStatus(status);
        return comment;
    }

    private static class StubCommentService extends CommentService {
        private Function<String, List<Comment>> listHandler = status -> List.of();
                private Function<UpdateCommentStatusRequest, Comment> updateStatusHandler = request -> null;

        @Override
        public List<Comment> list(String status) {
            return listHandler.apply(status);
        }

        @Override
        public Comment updateStatus(UpdateCommentStatusRequest request) {
            return updateStatusHandler.apply(request);
        }
    }
}
