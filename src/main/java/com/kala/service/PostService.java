package com.kala.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.kala.api.error.ApiException;
import com.kala.api.request.CreatePostRequest;
import com.kala.api.request.UpdatePostRequest;
import com.kala.api.request.UpdatePostStatusRequest;
import com.kala.model.Post;
import com.kala.model.PostStatus;
import com.kala.model.ProductType;
import com.kala.repository.PostRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PostService {

    @Inject
    PostRepository postRepository;

    public Post create(CreatePostRequest request) {
        validateCreateRequest(request);

        if (postRepository.findByPostId(request.getPostId().trim()).isPresent()) {
            throw new ApiException(Response.Status.CONFLICT, "post already exists");
        }

        Post post = new Post();
        post.setPostId(request.getPostId().trim());
        post.setProduct(ProductType.fromString(request.getProduct()));
        post.setRegisterDate(Instant.now());
        post.setStatus(PostStatus.ACTIVE);

        return postRepository.insert(post);
    }

    public List<Post> list(String status) {
        if (status == null || status.isBlank()) {
            return postRepository.findAll(Optional.empty());
        }

        return postRepository.findAll(Optional.of(PostStatus.fromString(status)));
    }

    public Post update(String postId, UpdatePostRequest request) {
        if (postId == null || postId.isBlank()) {
            throw new IllegalArgumentException("postId is required");
        }

        if (request == null) {
            throw new IllegalArgumentException("request body is required");
        }

        Post existingPost = postRepository.findByPostId(postId.trim())
                .orElseThrow(() -> new ApiException(Response.Status.NOT_FOUND, "post not found"));

        if (request.getProduct() == null || request.getProduct().isBlank()) {
            throw new IllegalArgumentException("product is required");
        }

        existingPost.setProduct(ProductType.fromString(request.getProduct()));
        return postRepository.update(existingPost);
    }

    public Post updateStatus(String postId, UpdatePostStatusRequest request) {
        if (postId == null || postId.isBlank()) {
            throw new IllegalArgumentException("postId is required");
        }

        if (request == null) {
            throw new IllegalArgumentException("request body is required");
        }

        Post existingPost = postRepository.findByPostId(postId.trim())
                .orElseThrow(() -> new ApiException(Response.Status.NOT_FOUND, "post not found"));

        existingPost.setStatus(PostStatus.fromString(request.getStatus()));
        return postRepository.update(existingPost);
    }

    private void validateCreateRequest(CreatePostRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request body is required");
        }

        if (request.getPostId() == null || request.getPostId().isBlank()) {
            throw new IllegalArgumentException("post_id is required");
        }

        if (request.getProduct() == null || request.getProduct().isBlank()) {
            throw new IllegalArgumentException("product is required");
        }
    }
}
