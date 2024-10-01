package com.example.service.impl;

import com.example.model.Rating;
import com.example.model.User;
import com.example.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    public void register(String email, String password) {
        logger.info("Starting registration for email: {}", email);

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        try {
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            logger.info("User registered successfully with UID: {}", userRecord.getUid());
            saveToDatabase(userRecord);
        } catch (FirebaseAuthException e) {
            logger.error("Error registering user with email: {}: {}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveToDatabase(UserRecord userRecord) {
        logger.info("Saving user to database with UID: {}", userRecord.getUid());

        User user = new User();
        user.setEmail(userRecord.getEmail());
        user.setRating(new Rating(0, 0, 0));

        usersRef.child(userRecord.getUid()).setValue(user, (error, ref) -> {
            if (error == null) {
                logger.info("User saved successfully to database with UID: {}", userRecord.getUid());
            } else {
                logger.error("Error saving user to database with UID: {}: {}", userRecord.getUid(), error.getMessage());
            }
        });
    }

    @Override
    public void assignUserRole(String uid, String role) {
        logger.info("Assigning role '{}' to user with UID: {}", role, uid);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // e.g., "admin", "user", "moderator"

        try {
            FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
            logger.info("Role '{}' assigned successfully to user with UID: {}", role, uid);
        } catch (FirebaseAuthException e) {
            logger.error("Error assigning role '{}' to user with UID: {}: {}", role, uid, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
