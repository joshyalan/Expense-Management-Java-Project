package dao;

import model.Expense;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAOImpl implements ExpenseDAO {

    @Override
    public boolean addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (user_id, account_id, category_id, amount, date, time, description, receipt_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expense.getUserId());
            pstmt.setInt(2, expense.getAccountId());
            pstmt.setInt(3, expense.getCategoryId());
            pstmt.setDouble(4, expense.getAmount());
            pstmt.setDate(5, expense.getDate());
            pstmt.setTime(6, expense.getTime());
            pstmt.setString(7, expense.getDescription());
            pstmt.setString(8, expense.getReceiptPath());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET account_id = ?, category_id = ?, amount = ?, date = ?, time = ?, description = ?, receipt_path = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expense.getAccountId());
            pstmt.setInt(2, expense.getCategoryId());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setDate(4, expense.getDate());
            pstmt.setTime(5, expense.getTime());
            pstmt.setString(6, expense.getDescription());
            pstmt.setString(7, expense.getReceiptPath());
            pstmt.setInt(8, expense.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Expense getExpenseById(int expenseId) {
        String sql = "SELECT * FROM expenses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Expense(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"), rs.getInt("category_id"),
                        rs.getDouble("amount"), rs.getDate("date"), rs.getTime("time"), rs.getString("description"), rs.getString("receipt_path"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getAllExpensesByUser(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(new Expense(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"), rs.getInt("category_id"),
                        rs.getDouble("amount"), rs.getDate("date"), rs.getTime("time"), rs.getString("description"), rs.getString("receipt_path"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public List<Expense> getExpensesByDateRange(int userId, Date startDate, Date endDate) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(new Expense(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"), rs.getInt("category_id"),
                        rs.getDouble("amount"), rs.getDate("date"), rs.getTime("time"), rs.getString("description"), rs.getString("receipt_path"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public List<Expense> getExpensesByCategory(int userId, int categoryId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? AND category_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(new Expense(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"), rs.getInt("category_id"),
                        rs.getDouble("amount"), rs.getDate("date"), rs.getTime("time"), rs.getString("description"), rs.getString("receipt_path"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
}
