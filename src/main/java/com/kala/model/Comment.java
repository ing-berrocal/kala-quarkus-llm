package com.kala.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {

    @JsonProperty("comment_id")
    private String commentId;

    @JsonProperty("post_id")
    private String postId;

    @JsonProperty("parent_id")
    private String parentId;

    private CommentDetail comment;

    private CommentResponse response;

    private CommentStatus status;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public CommentDetail getComment() {
        return comment;
    }

    public void setComment(CommentDetail comment) {
        this.comment = comment;
    }

    public CommentResponse getResponse() {
        return response;
    }

    public void setResponse(CommentResponse response) {
        this.response = response;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public static class CommentDetail {
        private CommentFrom from;

        private String message;

        @JsonProperty("created_time")
        private Long createdTime;

        public CommentFrom getFrom() {
            return from;
        }

        public void setFrom(CommentFrom from) {
            this.from = from;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Long createdTime) {
            this.createdTime = createdTime;
        }
    }

    public static class CommentFrom {
        private String id;

        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class CommentResponse {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
