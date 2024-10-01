package com.example.service;


import com.example.model.Review;

public interface ReviewService {
    void review(String userId, Review review);

    void deleteReview (String userId, String reviewId);
}
