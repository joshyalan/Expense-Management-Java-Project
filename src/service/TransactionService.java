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
            if (t1.getDate().equals(t2.getDate())) {
                // If dates are equal, sort by created timestamp if available, else maintain order
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
        allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        return allTransactions;
    }
}
