package com.example.service.impl;


import com.example.model.Rating;
import com.example.model.Review;
import com.example.service.ReviewService;
import com.google.firebase.database.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Override
    public void review(String userId, Review review) {
        logger.info("Adding review for user with ID: {}", userId);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("reviews").child(userId);

        String reviewId = reviewsRef.push().getKey();
        review.setReviewId(reviewId);

        logger.debug("Generated review ID: {}", reviewId);

        reviewsRef.child(reviewId).setValueAsync(review);
        logger.info("Review added for user: {} with review ID: {}", userId, reviewId);

        userRef.child("rating").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() == null) {
                    logger.warn("No rating data found for user: {}", userId);
                    return Transaction.success(currentData);
                }

                Rating rating = currentData.getValue(Rating.class);
                if (rating == null) {
                    rating = new Rating(0, 0, 0);
                    logger.info("No rating data present. Initializing new rating.");
                }

                rating.setSum(rating.getSum() + review.getRating());
                rating.setCount(rating.getCount() + 1);
                rating.setAverage((double) rating.getSum() / rating.getCount());

                logger.debug("Updated rating for user: {} - Sum: {}, Count: {}, Average: {}", userId, rating.getSum(), rating.getCount(), rating.getAverage());

                currentData.setValue(rating);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (committed) {
                    logger.info("Rating transaction committed successfully for user: {}", userId);
                } else {
                    logger.error("Rating transaction failed for user: {} with error: {}", userId, error.getMessage());
                }
            }
        });

    }

    @Override
    public void deleteReview(String userId, String reviewId) {
        logger.info("Deleting review with ID: {} for user: {}", reviewId, userId);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("reviews").child(userId).child(reviewId);

        reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Review review = dataSnapshot.getValue(Review.class);
                    reviewsRef.removeValueAsync();
                    logger.info("Review deleted: {} for user: {}", reviewId, userId);

                    userRef.child("rating").runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData currentData) {
                            if (currentData.getValue() == null) {
                                logger.warn("No rating data found for user: {}", userId);
                                return Transaction.success(currentData);
                            }

                            Rating rating = currentData.getValue(Rating.class);
                            if (rating == null) {
                                rating = new Rating(0, 0, 0);
                                logger.info("No rating data present. Initializing new rating.");
                            }

                            rating.setSum(rating.getSum() - review.getRating());
                            rating.setCount(rating.getCount() - 1);
                            if (rating.getCount() > 0) {
                                rating.setAverage((double) rating.getSum() / rating.getCount());
                            } else {
                                rating.setAverage(0);
                            }

                            logger.debug("Updated rating after review deletion for user: {} - Sum: {}, Count: {}, Average: {}", userId, rating.getSum(), rating.getCount(), rating.getAverage());

                            currentData.setValue(rating);
                            return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                            if (committed) {
                                logger.info("Rating transaction after review deletion committed successfully for user: {}", userId);
                            } else {
                                logger.error("Rating transaction after review deletion failed for user: {} with error: {}", userId, error.getMessage());
                            }
                        }
                    });
                } else {
                    logger.warn("Review with ID: {} not found for user: {}", reviewId, userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.error("Error retrieving review with ID: {} for user: {} - {}", reviewId, userId, databaseError.getMessage());
            }
        });
    }
}
