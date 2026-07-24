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

    /**
     * Gets a new database connection instance.
     * 
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            // Explicitly load the MySQL driver (optional in newer JDBC versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found. Please add the JDBC jar to your project.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed. Check your URL, username, and password.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Closes the database connection.
     * (Deprecated: Connections are now managed via try-with-resources in DAOs)
     */
    public static void closeConnection() {
        // Obsolete
    }
}
