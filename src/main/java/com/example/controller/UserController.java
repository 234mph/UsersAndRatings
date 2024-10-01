package com.example.controller;


import com.example.model.RegistrationForm;
import com.example.model.Review;
import com.example.service.ReviewService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private UserService userService;
    private ReviewService reviewService;

    public UserController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/users")
    public ResponseEntity register(@RequestBody RegistrationForm registrationForm) {
        userService.register(registrationForm.getEmail(), registrationForm.getPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/users/{userId}/reviews")
    public ResponseEntity review(@PathVariable String userId, @RequestBody Review review) {
        reviewService.review(userId, review);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}/reviews/{reviewId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity deleteReview(@PathVariable String userId, @PathVariable String reviewId) {
        reviewService.deleteReview(userId, reviewId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/users/{userId}")
    public ResponseEntity assignRole(@PathVariable String userId,@RequestParam String role) {
        userService.assignUserRole(userId,role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}