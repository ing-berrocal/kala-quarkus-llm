package com.kala.api;

import java.util.List;

import com.kala.api.request.CreatePostRequest;
import com.kala.api.request.UpdatePostRequest;
import com.kala.api.request.UpdatePostStatusRequest;
import com.kala.model.Post;
import com.kala.service.PostService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    PostService postService;

    @POST
    public Post create(CreatePostRequest request) {
        return postService.create(request);
    }

    @GET
    public List<Post> list(@QueryParam("status") String status) {
        return postService.list(status);
    }

    @PUT
    @Path("/{postId}")
    public Post update(@PathParam("postId") String postId, UpdatePostRequest request) {
        return postService.update(postId, request);
    }

    @PATCH
    @Path("/{postId}/status")
    public Post updateStatus(@PathParam("postId") String postId, UpdatePostStatusRequest request) {
        return postService.updateStatus(postId, request);
    }
}
