package utils;

import model.User;

/**
 * Singleton class to manage the currently logged-in user's session.
 */
public class SessionManager {
    private static User currentUser = null;

    /**
     * Stores the authenticated user in the session.
     */
    public static void login(User user) {
        currentUser = user;
    }

    /**
     * Clears the session on logout.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Retrieves the currently logged-in user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
