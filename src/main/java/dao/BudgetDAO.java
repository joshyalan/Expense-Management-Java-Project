package dao;

import model.Budget;
import java.util.List;

public interface BudgetDAO {
    boolean addBudget(Budget budget);
    boolean updateBudget(Budget budget);
    boolean deleteBudget(int budgetId);
    Budget getBudgetById(int budgetId);
    Budget getBudgetByCategoryAndMonth(int userId, int categoryId, int month, int year);
    List<Budget> getAllBudgetsByUser(int userId);
    List<Budget> getBudgetsByMonth(int userId, int month, int year);
}
