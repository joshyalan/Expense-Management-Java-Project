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
}
