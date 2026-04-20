package com.kala.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateCommentStatusRequest {

    @JsonProperty("comment_id")
    private String commentId;

    private String status;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
