package com.kala.api;

import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kala.api.error.ApiException;
import com.kala.api.request.CreatePostRequest;
import com.kala.api.request.UpdatePostRequest;
import com.kala.api.request.UpdatePostStatusRequest;
import com.kala.model.Post;
import com.kala.model.PostStatus;
import com.kala.model.ProductType;
import com.kala.service.PostService;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class PostResourceTest {

    @BeforeEach
    void resetServiceMock() {
        QuarkusMock.installMockForType(new StubPostService(), PostService.class);
    }

    @Test
    void shouldCreatePost() {
        StubPostService stub = new StubPostService();
        stub.createHandler = request -> buildPost("123_1", ProductType.CAMISAS, PostStatus.ACTIVE);
        QuarkusMock.installMockForType(stub, PostService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"post_id\":\"123_1\",\"product\":\"CAMISAS\"}")
        .when()
                .post("/posts")
        .then()
                .statusCode(200)
                .body("post_id", equalTo("123_1"))
                .body("product", equalTo("CAMISAS"))
                .body("status", equalTo("ACTIVE"));
    }

    @Test
    void shouldListPosts() {
        StubPostService stub = new StubPostService();
        stub.listHandler = status -> List.of(
                buildPost("123_1", ProductType.CAMISAS, PostStatus.ACTIVE),
                buildPost("123_2", ProductType.BODIES, PostStatus.INACTIVE));
        QuarkusMock.installMockForType(stub, PostService.class);

        given()
                .queryParam("status", "ACTIVE")
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("size()", equalTo(2))
                .body("[0].post_id", equalTo("123_1"))
                .body("[1].post_id", equalTo("123_2"));
    }

    @Test
    void shouldUpdatePost() {
        StubPostService stub = new StubPostService();
        stub.updateHandler = (postId, request) -> buildPost(postId, ProductType.BODIES, PostStatus.ACTIVE);
        QuarkusMock.installMockForType(stub, PostService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"product\":\"BODIES\"}")
        .when()
                .put("/posts/123_1")
        .then()
                .statusCode(200)
                .body("post_id", equalTo("123_1"))
                .body("product", equalTo("BODIES"));
    }

    @Test
    void shouldUpdatePostStatus() {
        StubPostService stub = new StubPostService();
        stub.updateStatusHandler = (postId, request) -> buildPost(postId, ProductType.CAMISAS, PostStatus.INACTIVE);
        QuarkusMock.installMockForType(stub, PostService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"status\":\"INACTIVE\"}")
        .when()
                .patch("/posts/123_1/status")
        .then()
                .statusCode(200)
                .body("post_id", equalTo("123_1"))
                .body("status", equalTo("INACTIVE"));
    }

    @Test
    void shouldReturnValidationError() {
        StubPostService stub = new StubPostService();
        stub.createHandler = request -> {
            throw new IllegalArgumentException("post_id is required");
        };
        QuarkusMock.installMockForType(stub, PostService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"product\":\"CAMISAS\"}")
        .when()
                .post("/posts")
        .then()
                .statusCode(400)
                .body("message", equalTo("post_id is required"));
    }

    @Test
    void shouldReturnNotFoundError() {
        StubPostService stub = new StubPostService();
        stub.updateHandler = (postId, request) -> {
            throw new ApiException(Response.Status.NOT_FOUND, "post not found");
        };
        QuarkusMock.installMockForType(stub, PostService.class);

        given()
                .contentType(ContentType.JSON)
                .body("{\"product\":\"CAMISAS\"}")
        .when()
                .put("/posts/post_missing")
        .then()
                .statusCode(404)
                .body("message", equalTo("post not found"));
    }

    private static Post buildPost(String postId, ProductType productType, PostStatus status) {
        Post post = new Post();
        post.setPostId(postId);
        post.setProduct(productType);
        post.setStatus(status);
        post.setRegisterDate(Instant.parse("2026-04-19T00:00:00Z"));
        return post;
    }

    private static class StubPostService extends PostService {
        private Function<CreatePostRequest, Post> createHandler = request -> null;
        private Function<String, List<Post>> listHandler = status -> List.of();
        private BiFunction<String, UpdatePostRequest, Post> updateHandler = (postId, request) -> null;
        private BiFunction<String, UpdatePostStatusRequest, Post> updateStatusHandler = (postId, request) -> null;

        @Override
        public Post create(CreatePostRequest request) {
            return createHandler.apply(request);
        }

        @Override
        public List<Post> list(String status) {
            return listHandler.apply(status);
        }

        @Override
        public Post update(String postId, UpdatePostRequest request) {
            return updateHandler.apply(postId, request);
        }

        @Override
        public Post updateStatus(String postId, UpdatePostStatusRequest request) {
            return updateStatusHandler.apply(postId, request);
        }
    }
}
