package com.kala.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.kala.model.Post;
import com.kala.model.PostStatus;
import com.kala.model.ProductType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PostRepository {

    private static final String COLLECTION_NAME = "posts";

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database", defaultValue = "kala")
    String databaseName;

    public Optional<Post> findByPostId(String postId) {
        Document document = collection().find(Filters.eq("post_id", postId)).first();
        if (document == null) {
            return Optional.empty();
        }
        return Optional.of(toPost(document));
    }

    public List<Post> findAll(Optional<PostStatus> status) {
        List<Post> posts = new ArrayList<>();

        if (status.isPresent()) {
            collection().find(Filters.eq("status", status.get().name()))
                    .sort(Sorts.descending("register_date"))
                    .forEach(doc -> posts.add(toPost(doc)));
        } else {
            collection().find()
                    .sort(Sorts.descending("register_date"))
                    .forEach(doc -> posts.add(toPost(doc)));
        }

        return posts;
    }

    public Post insert(Post post) {
        collection().insertOne(toDocument(post));
        return post;
    }

    public Post update(Post post) {
        collection().replaceOne(Filters.eq("post_id", post.getPostId()), toDocument(post));
        return post;
    }

    private MongoCollection<Document> collection() {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        return database.getCollection(COLLECTION_NAME);
    }

    private Document toDocument(Post post) {
        return new Document()
                .append("post_id", post.getPostId())
                .append("product", post.getProduct().name())
                .append("register_date", post.getRegisterDate().toString())
                .append("status", post.getStatus().name());
    }

    private Post toPost(Document document) {
        Post post = new Post();
        post.setPostId(document.getString("post_id"));
        post.setProduct(ProductType.fromString(document.getString("product")));
        post.setRegisterDate(Instant.parse(document.getString("register_date")));
        post.setStatus(PostStatus.fromString(document.getString("status")));
        return post;
    }
}
