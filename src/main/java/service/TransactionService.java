package service;

import dao.ExpenseDAOImpl;
import dao.IncomeDAOImpl;
import model.Expense;
import model.Income;
import model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionService {

    private ExpenseDAOImpl expenseDAO;
    private IncomeDAOImpl incomeDAO;

    public TransactionService() {
        this.expenseDAO = new ExpenseDAOImpl();
        this.incomeDAO = new IncomeDAOImpl();
    }

    public List<Transaction> getRecentTransactionsByUser(int userId, int limit) {
        List<Expense> expenses = expenseDAO.getAllExpensesByUser(userId);
        List<Income> incomes = incomeDAO.getAllIncomeByUser(userId);

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(expenses);
        allTransactions.addAll(incomes);

        // Sort by date descending
        allTransactions.sort((t1, t2) -> {
            if (t1.getDate() == null && t2.getDate() == null) return 0;
            if (t1.getDate() == null) return 1;
            if (t2.getDate() == null) return -1;
            
            if (t1.getDate().equals(t2.getDate())) {
                if (t1.getTime() != null && t2.getTime() != null) {
                    if (!t1.getTime().equals(t2.getTime())) {
                        return t2.getTime().compareTo(t1.getTime());
                    }
                }
                
                // If dates and times are equal, sort by created timestamp if available, else maintain order
                if (t1.getCreatedAt() != null && t2.getCreatedAt() != null) {
                    return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                }
                return 0;
            }
            return t2.getDate().compareTo(t1.getDate());
        });

        if (allTransactions.size() > limit) {
            return allTransactions.subList(0, limit);
        }
        return allTransactions;
    }

    public List<Transaction> getAllTransactionsByUser(int userId) {
        List<Expense> expenses = expenseDAO.getAllExpensesByUser(userId);
        List<Income> incomes = incomeDAO.getAllIncomeByUser(userId);

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(expenses);
        allTransactions.addAll(incomes);

        // Sort by date descending
        allTransactions.sort((t1, t2) -> {
            if (t1.getDate() == null && t2.getDate() == null) return 0;
            if (t1.getDate() == null) return 1;
            if (t2.getDate() == null) return -1;
            
            if (t1.getDate().equals(t2.getDate())) {
                if (t1.getTime() != null && t2.getTime() != null) {
                    if (!t1.getTime().equals(t2.getTime())) {
                        return t2.getTime().compareTo(t1.getTime());
                    }
                }
                if (t1.getCreatedAt() != null && t2.getCreatedAt() != null) {
                    return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                }
                return 0;
            }
            
            return t2.getDate().compareTo(t1.getDate());
        });

        return allTransactions;
    }
}
