package com.kala.service;

import java.util.List;
import java.util.Optional;

import com.kala.api.error.ApiException;
import com.kala.api.request.UpdateCommentStatusRequest;
import com.kala.model.Comment;
import com.kala.model.CommentStatus;
import com.kala.repository.CommentRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    public List<Comment> list(String status) {
        if (status == null || status.isBlank()) {
            return commentRepository.findAll(Optional.empty());
        }

        return commentRepository.findAll(Optional.of(CommentStatus.fromString(status)));
    }

    public Comment updateStatus(UpdateCommentStatusRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request body is required");
        }

        if (request.getCommentId() == null || request.getCommentId().isBlank()) {
            throw new IllegalArgumentException("comment_id is required");
        }

        Comment comment = commentRepository.findByCommentId(request.getCommentId().trim())
                .orElseThrow(() -> new ApiException(Response.Status.NOT_FOUND, "comment not found"));

        comment.setStatus(CommentStatus.fromString(request.getStatus()));
        return commentRepository.update(comment);
    }
}
