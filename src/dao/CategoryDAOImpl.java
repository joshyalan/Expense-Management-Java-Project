package dao;

import model.Category;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl {
    
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO categories (user_id, name, type, icon, color) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, category.getUserId());
            pstmt.setString(2, category.getName());
            pstmt.setString(3, category.getType());
            pstmt.setString(4, category.getIcon());
            pstmt.setString(5, category.getColor());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Category> getCategoriesByUserAndType(int userId, String type) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE user_id = ? AND type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, type);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(
                        rs.getInt("id"), rs.getInt("user_id"), 
                        rs.getString("name"), rs.getString("type"), 
                        rs.getString("icon"), rs.getString("color"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Seeds predefined standard categories for a new user.
     */
    public boolean seedDefaultCategories(int userId) {
        String sql = "INSERT INTO categories (user_id, name, type, icon, color) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            Object[][] defaults = {
                // Expenses
                {"Food & Dining", "EXPENSE", "utensils", "#E63946"},
                {"Groceries", "EXPENSE", "shopping-cart", "#F4A261"},
                {"Transportation", "EXPENSE", "car", "#2A9D8F"},
                {"Fuel", "EXPENSE", "gas-pump", "#E76F51"},
                {"Shopping", "EXPENSE", "bag", "#E9C46A"},
                {"Entertainment", "EXPENSE", "film", "#8338EC"},
                {"Bills & Utilities", "EXPENSE", "bolt", "#3A86FF"},
                {"Rent", "EXPENSE", "home", "#1D3557"},
                {"Healthcare", "EXPENSE", "medkit", "#D90429"},
                {"Education", "EXPENSE", "book", "#FB8500"},
                {"Travel", "EXPENSE", "plane", "#00B4D8"},
                {"Personal Care", "EXPENSE", "heart", "#FFB5A7"},
                {"Subscriptions", "EXPENSE", "sync", "#9D4EDD"},
                {"Miscellaneous", "EXPENSE", "box", "#6C757D"},
                // Income
                {"Salary", "INCOME", "briefcase", "#2A9D8F"},
                {"Freelance", "INCOME", "laptop", "#F4A261"},
                {"Bonus", "INCOME", "star", "#E9C46A"},
                {"Investments", "INCOME", "chart-line", "#3A86FF"},
                {"Interest", "INCOME", "percent", "#8338EC"},
                {"Gifts", "INCOME", "gift", "#FF006E"},
                {"Refunds", "INCOME", "undo", "#00B4D8"}
            };
            
            for (Object[] cat : defaults) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, (String) cat[0]);
                pstmt.setString(3, (String) cat[1]);
                pstmt.setString(4, (String) cat[2]);
                pstmt.setString(5, (String) cat[3]);
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
