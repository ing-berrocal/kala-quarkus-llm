package com.kala.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post {

    @JsonProperty("post_id")
    private String postId;

    private ProductType product;

    @JsonProperty("register_date")
    private Instant registerDate;

    private PostStatus status;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public ProductType getProduct() {
        return product;
    }

    public void setProduct(ProductType product) {
        this.product = product;
    }

    public Instant getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Instant registerDate) {
        this.registerDate = registerDate;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }
}
