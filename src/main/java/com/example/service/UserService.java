package com.example.service;

import com.google.firebase.auth.UserRecord;

public interface UserService {
    void register(String email, String password);

    void saveToDatabase(UserRecord userRecord);

    void assignUserRole(String uid, String role);

}