package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to manage database connections.
 * This ensures we only have one active connection instance at a time,
 * saving memory and preventing database lockups.
 */
public class DBConnection {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker_db";
    private static final String USER = "root";       // Change this to your MySQL username if different
    private static final String PASSWORD = "291006"; // Change this to your MySQL password

    // Static variable to hold the single connection instance
    private static Connection connection = null;

    // Private constructor prevents instantiation from other classes
    private DBConnection() {
    }

    /**
     * Gets the database connection instance.
     * If it doesn't exist or is closed, it creates a new one.
     * 
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Explicitly load the MySQL driver (optional in newer JDBC versions, but good practice)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found. Please add the JDBC jar to your project.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed. Check your URL, username, and password.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
