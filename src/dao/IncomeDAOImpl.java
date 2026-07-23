package dao;

import model.Income;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAOImpl implements IncomeDAO {

    @Override
    public boolean addIncome(Income income) {
        String sql = "INSERT INTO income (user_id, account_id, category_id, amount, date, description, receipt_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income.getUserId());
            pstmt.setInt(2, income.getAccountId());
            pstmt.setInt(3, income.getCategoryId());
            pstmt.setDouble(4, income.getAmount());
            pstmt.setDate(5, income.getDate());
            pstmt.setString(6, income.getDescription());
            pstmt.setString(7, income.getReceiptPath());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateIncome(Income income) {
        String sql = "UPDATE income SET account_id = ?, category_id = ?, amount = ?, date = ?, description = ?, receipt_path = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income.getAccountId());
            pstmt.setInt(2, income.getCategoryId());
            pstmt.setDouble(3, income.getAmount());
            pstmt.setDate(4, income.getDate());
            pstmt.setString(5, income.getDescription());
            pstmt.setString(6, income.getReceiptPath());
            pstmt.setInt(7, income.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteIncome(int incomeId) {
        String sql = "DELETE FROM income WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, incomeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Income getIncomeById(int incomeId) {
        String sql = "SELECT * FROM income WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, incomeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractIncomeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Income> getAllIncomeByUser(int userId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM income WHERE user_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    incomes.add(extractIncomeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }

    @Override
    public List<Income> getIncomeByDateRange(int userId, Date startDate, Date endDate) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM income WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    incomes.add(extractIncomeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }

    @Override
    public List<Income> getIncomeByCategory(int userId, int categoryId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM income WHERE user_id = ? AND category_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    incomes.add(extractIncomeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }

    private Income extractIncomeFromResultSet(ResultSet rs) throws SQLException {
        return new Income(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("account_id"),
            rs.getInt("category_id"),
            rs.getDouble("amount"),
            rs.getDate("date"),
            rs.getString("description"),
            rs.getString("receipt_path"),
            rs.getTimestamp("created_at")
        );
    }
}
