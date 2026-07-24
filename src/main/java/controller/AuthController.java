package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;
import utils.SessionManager;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Controller to handle authentication logic (Login and Registration).
 * Separates GUI actions from Database logic (MVC architecture).
 */
public class AuthController {
    private UserDAO userDAO;

    public AuthController() {
        // Instantiate the Data Access Object
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Attempts to log a user in.
     * @param username The username input
     * @param password The raw password input
     * @return true if successful, false otherwise
     */
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        User user = userDAO.getUserByUsername(username);

        if (user != null) {
            try {
                if (BCrypt.checkpw(password, user.getPasswordHash())) {
                    SessionManager.login(user); // Store session
                    return true;
                }
            } catch (Exception e) {
                // If the stored password isn't a valid BCrypt hash (e.g., from old schema)
                System.err.println("Password verification error: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * Registers a new user with a hashed password.
     */
    public boolean register(String username, String password, String email, String fullName) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            return false; // Validation failed
        }

        // Hash the password with BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        
        User newUser = new User(username, hashedPassword, email, fullName, "USER");
        boolean registered = userDAO.registerUser(newUser);
        
        if (registered) {
            // Seed default categories
            User createdUser = userDAO.getUserByUsername(username);
            if (createdUser != null) {
                dao.CategoryDAOImpl categoryDAO = new dao.CategoryDAOImpl();
                categoryDAO.seedDefaultCategories(createdUser.getId());
                
                // Seed a default account
                dao.AccountDAOImpl accountDAO = new dao.AccountDAOImpl();
                model.Account defaultAccount = new model.Account(createdUser.getId(), "Main Wallet", 0.0, "USD");
                accountDAO.addAccount(defaultAccount);
            }
            return true;
        }
        return false;
    }
}
