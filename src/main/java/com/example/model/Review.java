package com.example.model;

import java.time.Instant;

public class Review {
    private String reviewId;     // Unique ID for the review
    private String fromUserId;   // The user who wrote the review
    private int rating;          // Rating score (e.g., 1-5)
    private String comment;      // Review comment
    private long timestamp;      // Timestamp when the review was created (Epoch time)

    public Review() {
    }

    public Review(String fromUserId, int rating, String comment) {
        this.fromUserId = fromUserId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}