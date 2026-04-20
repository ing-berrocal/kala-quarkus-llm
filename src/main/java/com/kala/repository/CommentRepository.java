package com.kala.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.kala.model.Comment;
import com.kala.model.CommentStatus;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CommentRepository {

    private static final String COLLECTION_NAME = "post_comment";

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database", defaultValue = "kala")
    String databaseName;

    public Optional<Comment> findByCommentId(String commentId) {
        Document document = collection().find(Filters.eq("comment_id", commentId)).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(toComment(document));
    }

    public List<Comment> findAll(Optional<CommentStatus> status) {
        List<Comment> comments = new ArrayList<>();

        if (status.isPresent()) {
            collection().find(Filters.eq("status", status.get().name()))
                    .sort(Sorts.descending("comment.created_time"))
                    .forEach(doc -> comments.add(toComment(doc)));
        } else {
            collection().find()
                    .sort(Sorts.descending("comment.created_time"))
                    .forEach(doc -> comments.add(toComment(doc)));
        }

        return comments;
    }

    public Comment update(Comment comment) {
        collection().replaceOne(Filters.eq("comment_id", comment.getCommentId()), toDocument(comment));
        return comment;
    }

    private MongoCollection<Document> collection() {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        return database.getCollection(COLLECTION_NAME);
    }

    private Document toDocument(Comment comment) {
        Document document = new Document()
                .append("comment_id", comment.getCommentId())
                .append("post_id", comment.getPostId())
                .append("parent_id", comment.getParentId())
                .append("status", comment.getStatus() != null ? comment.getStatus().name() : null);

        if (comment.getComment() != null) {
            Document commentDoc = new Document()
                    .append("message", comment.getComment().getMessage())
                    .append("created_time", comment.getComment().getCreatedTime());

            if (comment.getComment().getFrom() != null) {
                commentDoc.append("from", new Document()
                        .append("id", comment.getComment().getFrom().getId())
                        .append("name", comment.getComment().getFrom().getName()));
            }

            document.append("comment", commentDoc);
        }

        if (comment.getResponse() != null) {
            document.append("response", new Document().append("message", comment.getResponse().getMessage()));
        }

        return document;
    }

    private Comment toComment(Document document) {
        Comment comment = new Comment();
        comment.setCommentId(document.getString("comment_id"));
        comment.setPostId(document.getString("post_id"));
        comment.setParentId(document.getString("parent_id"));
        comment.setStatus(CommentStatus.fromString(document.getString("status")));

        Document commentDoc = document.get("comment", Document.class);
        if (commentDoc != null) {
            Comment.CommentDetail detail = new Comment.CommentDetail();
            detail.setMessage(commentDoc.getString("message"));

            Number createdTime = commentDoc.get("created_time", Number.class);
            if (createdTime != null) {
                detail.setCreatedTime(createdTime.longValue());
            }

            Document fromDoc = commentDoc.get("from", Document.class);
            if (fromDoc != null) {
                Comment.CommentFrom from = new Comment.CommentFrom();
                from.setId(fromDoc.getString("id"));
                from.setName(fromDoc.getString("name"));
                detail.setFrom(from);
            }

            comment.setComment(detail);
        }

        Document responseDoc = document.get("response", Document.class);
        if (responseDoc != null) {
            Comment.CommentResponse response = new Comment.CommentResponse();
            response.setMessage(responseDoc.getString("message"));
            comment.setResponse(response);
        }

        return comment;
    }
}
