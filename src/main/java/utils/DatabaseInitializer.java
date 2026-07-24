package utils;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String createAccounts = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "name VARCHAR(100) NOT NULL, " +
                "balance DECIMAL(12, 2) DEFAULT 0.00, " +
                "currency VARCHAR(10) DEFAULT 'USD', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";
            stmt.execute(createAccounts);
            System.out.println("Accounts table verified.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
