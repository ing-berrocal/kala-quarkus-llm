package com.kala.api;

import java.util.List;

import com.kala.api.request.UpdateCommentStatusRequest;
import com.kala.model.Comment;
import com.kala.service.CommentService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Inject
    CommentService commentService;

    @GET
    public List<Comment> list(@QueryParam("status") String status) {
        return commentService.list(status);
    }

    @POST
    @Path("/status")
    public Comment updateStatus(UpdateCommentStatusRequest request) {
        return commentService.updateStatus(request);
    }
}
