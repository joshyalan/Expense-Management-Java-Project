package dao;

import model.User;

/**
 * Interface for User Data Access Object.
 * Defines the standard operations to be performed on User entities.
 * Demonstrates Abstraction in OOP.
 */
public interface UserDAO {
    /**
     * Registers a new user in the database.
     * @param user The user object to register.
     * @return true if successful, false otherwise.
     */
    boolean registerUser(User user);

    /**
     * Retrieves a user by their username (useful for authentication with BCrypt).
     * @param username The username.
     * @return The User object, or null if not found.
     */
    User getUserByUsername(String username);

    /**
     * Retrieves a user by their ID.
     * @param id The user ID.
     * @return User object.
     */
    User getUserById(int id);
    
    /**
     * Updates an existing user's profile information.
     * @param user The user object with updated fields.
     * @return true if successful, false otherwise.
     */
    boolean updateUser(User user);
    
    /**
     * Updates an existing user's password.
     * @param userId The user ID.
     * @param newPasswordHash The newly hashed password.
     * @return true if successful, false otherwise.
     */
    boolean updatePassword(int userId, String newPasswordHash);
}
