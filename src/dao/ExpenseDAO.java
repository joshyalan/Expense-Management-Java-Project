package dao;

import model.Expense;
import java.util.List;
import java.sql.Date;

/**
 * Interface for Expense Data Access Object.
 */
public interface ExpenseDAO {
    boolean addExpense(Expense expense);
    boolean updateExpense(Expense expense);
    boolean deleteExpense(int expenseId);
    Expense getExpenseById(int expenseId);
    
    // Retrieves all expenses for a specific user
    List<Expense> getAllExpensesByUser(int userId);
    
    // Retrieves expenses filtered by a specific date range for a user
    List<Expense> getExpensesByDateRange(int userId, Date startDate, Date endDate);
    
    // Retrieves expenses filtered by category
    List<Expense> getExpensesByCategory(int userId, int categoryId);
}
