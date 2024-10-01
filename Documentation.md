Project Documentation for User Reviews and Ratings System

Overview

This project is designed to manage a system of user reviews and ratings. Registered users can leave reviews and assign star ratings to other users. 
The ratings are calculated based on the star system, with the average rating automatically updated whenever a review is added or removed. 
Admin users have the ability to delete reviews, which triggers a recalculation of the user’s rating.

Key Features:
User Registration: Any user can register via email and password.
Review Submission: Users can submit text reviews and assign a rating (1-5 stars) to other users.
Rating Calculation: The average rating is automatically calculated and updated based on submitted reviews.
Admin Control: Admins can delete reviews, and the system recalculates the affected user’s rating.
Role Management: Users can be assigned roles like 'admin' or 'user'.
Firebase Integration:
The project utilizes Firebase for authentication and real-time database storage. 
Users are registered and managed via Firebase Authentication, and reviews and ratings are stored in the Firebase Realtime Database.

API Documentation

1. User Registration
Description:

Allows a new user to register by providing an email and password.

URL:

POST /users

Request Body:

email (string) — The user's email.
password (string) — The user's password.

Example Request:
{
  "email": "user@example.com",
  "password": "password123"
}
Responses:

201 Created — User successfully registered.
400 Bad Request — Invalid request.

Example cURL Command:
curl -X POST http://localhost:8080/users \
     -H "Content-Type: application/json" \
     -d '{"email": "user@example.com", "password": "password123"}'
2. Submit Review for User
Description:

Allows a user to submit a review and rating for another user. The average rating for the reviewed user is recalculated.

URL:

POST /users/{userId}/reviews

Path Parameters:

userId (string) — The ID of the user being reviewed.
Request Body:

json

{
  "comment": "Great user!",
  "rating": 5
}
Responses:

200 OK — Review successfully added.
400 Bad Request — Invalid request.

Example cURL Command:

bash

curl -X POST http://localhost:8080/users/user123/reviews \
     -H "Content-Type: application/json" \
     -d '{"text": "Great user!", "rating": 5}'
3. Delete Review (Admin Only)
Description:

Allows an admin to delete a specific review for a user. After deletion, the user’s rating is recalculated. 
This endpoint requires the user to have the admin role.

URL:

DELETE /users/{userId}/reviews/{reviewId}

Path Parameters:

userId (string) — The ID of the user whose review is being deleted.
reviewId (string) — The ID of the review to delete.
Responses:

200 OK — Review successfully deleted.
403 Forbidden — Access denied (requires admin role).

Example cURL Command:

bash

curl -X DELETE http://localhost:8080/users/user123/reviews/review456 \
     -H "Authorization: Bearer <admin-token>"
4. Assign Role to User
Description:

Assigns a specific role (e.g., admin or user) to a user.

URL:

POST /users/{userId}

Path Parameters:

userId (string) — The ID of the user to whom the role is being assigned.
Query Parameters:

role (string) — The role to assign (e.g., admin, user).
Responses:

200 OK — Role successfully assigned.
400 Bad Request — Invalid request.
Example cURL Command:

bash

curl -X POST "http://localhost:8080/users/user123?role=admin"
Firebase Integration Details

Firebase Authentication:
Used for handling user registrations and role management.
Admin users are identified via custom claims assigned using Firebase Authentication.

Firebase Realtime Database:
User Data: Stores user profiles including email and calculated rating.
Review Data: Stores reviews submitted for each user.
Ratings: The system stores and recalculates ratings for each user whenever a review is added or deleted.

Structure:

users: Stores user profiles, including rating.
reviews: Stores all reviews for each user.

Controller

UserController
This controller handles all user-related operations such as registration, review management, and role assignments.

Endpoints:

POST /users: Registers a new user.
POST /users/{userId}/reviews: Adds a review for the specified user.
DELETE /users/{userId}/reviews/{reviewId}: Allows an admin to delete a review.
POST /users/{userId}: Assigns a role to the specified user.

Security

Authentication: Handled via Firebase Authentication.
Authorization: Role-based access control is implemented, where only users with the admin role can delete reviews.
This document provides an overview of the system, its API, Firebase integration, 
and the core services that manage user reviews, ratings, and roles.
