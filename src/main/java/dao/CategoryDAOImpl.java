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

    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ?, type = ?, icon = ?, color = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getType());
            pstmt.setString(3, category.getIcon());
            pstmt.setString(4, category.getColor());
            pstmt.setInt(5, category.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Seeds predefined standard categories for a new user.
     */
    public boolean seedDefaultCategories(int userId) {
        String sql = "INSERT INTO categories (user_id, name, type, icon, color) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // First fetch existing to prevent duplicates
            List<String> existingExpense = new ArrayList<>();
            List<String> existingIncome = new ArrayList<>();
            for (Category c : getCategoriesByUserAndType(userId, "EXPENSE")) existingExpense.add(c.getName().toUpperCase());
            for (Category c : getCategoriesByUserAndType(userId, "INCOME")) existingIncome.add(c.getName().toUpperCase());
            
            Object[][] defaults = {
                // Expenses (25)
                {"Food & Dining", "EXPENSE", "utensils", "#FF4B4B"},
                {"Groceries", "EXPENSE", "shopping-cart", "#FF9F43"},
                {"Transportation", "EXPENSE", "car", "#2ED573"},
                {"Fuel", "EXPENSE", "gas-pump", "#FFA502"},
                {"Rent & Housing", "EXPENSE", "home", "#1E90FF"},
                {"Utilities", "EXPENSE", "bolt", "#3742FA"},
                {"Mobile & Internet", "EXPENSE", "wifi", "#70A1FF"},
                {"Shopping", "EXPENSE", "bag", "#FF6B81"},
                {"Entertainment", "EXPENSE", "film", "#A55EEA"},
                {"Travel", "EXPENSE", "plane", "#00A8FF"},
                {"Healthcare", "EXPENSE", "medkit", "#FF4757"},
                {"Education", "EXPENSE", "book", "#ECCC68"},
                {"Fitness", "EXPENSE", "dumbbell", "#2ED573"},
                {"Clothing", "EXPENSE", "tshirt", "#FF7F50"},
                {"Books", "EXPENSE", "book-open", "#DFA500"},
                {"Gaming", "EXPENSE", "gamepad", "#5352ED"},
                {"Gifts", "EXPENSE", "gift", "#FF6B81"},
                {"Family", "EXPENSE", "users", "#1E90FF"},
                {"Pets", "EXPENSE", "paw", "#FFA502"},
                {"Vehicle Maintenance", "EXPENSE", "wrench", "#A4B0BE"},
                {"Insurance", "EXPENSE", "shield", "#2ED573"},
                {"Loan / EMI", "EXPENSE", "file-invoice-dollar", "#FF4757"},
                {"Taxes", "EXPENSE", "landmark", "#57606F"},
                {"Subscriptions", "EXPENSE", "sync", "#A55EEA"},
                {"Miscellaneous", "EXPENSE", "box", "#747D8C"},
                // Income (10)
                {"Salary", "INCOME", "briefcase", "#2ED573"},
                {"Freelance", "INCOME", "laptop", "#1E90FF"},
                {"Business", "INCOME", "building", "#5352ED"},
                {"Investments", "INCOME", "chart-line", "#A55EEA"},
                {"Interest", "INCOME", "percent", "#70A1FF"},
                {"Gifts Received", "INCOME", "gift", "#FF6B81"},
                {"Refunds", "INCOME", "undo", "#FFA502"},
                {"Bonus", "INCOME", "star", "#ECCC68"},
                {"Pocket Money", "INCOME", "coins", "#2ED573"},
                {"Other Income", "INCOME", "plus-circle", "#747D8C"}
            };
            
            boolean addedAny = false;
            for (Object[] cat : defaults) {
                String name = (String) cat[0];
                String type = (String) cat[1];
                
                boolean exists = type.equals("EXPENSE") ? existingExpense.contains(name.toUpperCase()) : existingIncome.contains(name.toUpperCase());
                
                if (!exists) {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, name);
                    pstmt.setString(3, type);
                    pstmt.setString(4, (String) cat[2]);
                    pstmt.setString(5, (String) cat[3]);
                    pstmt.addBatch();
                    addedAny = true;
                }
            }
            
            if (addedAny) {
                pstmt.executeBatch();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
