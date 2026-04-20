package com.kala.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePostRequest {

    @JsonProperty("post_id")
    private String postId;

    private String product;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
