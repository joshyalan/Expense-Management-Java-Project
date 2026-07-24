package service;

import dao.ExpenseDAOImpl;
import dao.IncomeDAOImpl;
import dao.CategoryDAOImpl;
import model.Expense;
import model.Income;
import model.Category;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {
    private ExpenseDAOImpl expenseDAO = new ExpenseDAOImpl();
    private IncomeDAOImpl incomeDAO = new IncomeDAOImpl();
    private CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    public List<String> generateInsights(int userId) {
        List<String> insights = new ArrayList<>();
        List<Expense> expenses = expenseDAO.getAllExpensesByUser(userId);
        List<Income> incomes = incomeDAO.getAllIncomeByUser(userId);
        
        if (expenses.isEmpty()) {
            insights.add("Add some expenses to get AI spending insights.");
            return insights;
        }

        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);

        // Compare this month vs last month spending
        double thisMonthSpend = expenses.stream()
            .filter(e -> e.getDate().toLocalDate().getMonth() == now.getMonth() && e.getDate().toLocalDate().getYear() == now.getYear())
            .mapToDouble(Expense::getAmount)
            .sum();
            
        double lastMonthSpend = expenses.stream()
            .filter(e -> e.getDate().toLocalDate().getMonth() == lastMonth.getMonth() && e.getDate().toLocalDate().getYear() == lastMonth.getYear())
            .mapToDouble(Expense::getAmount)
            .sum();

        if (lastMonthSpend > 0) {
            if (thisMonthSpend > lastMonthSpend) {
                double percent = ((thisMonthSpend - lastMonthSpend) / lastMonthSpend) * 100;
                insights.add(String.format("You've spent %.1f%% more this month compared to last month.", percent));
            } else {
                double diff = lastMonthSpend - thisMonthSpend;
                insights.add(String.format("Great job! You saved $%.2f compared to last month.", diff));
            }
        }

        // Top Category
        Map<Integer, Double> catTotals = expenses.stream()
            .collect(Collectors.groupingBy(Expense::getCategoryId, Collectors.summingDouble(Expense::getAmount)));
            
        int topCatId = -1;
        double maxSpend = -1;
        for (Map.Entry<Integer, Double> entry : catTotals.entrySet()) {
            if (entry.getValue() > maxSpend) {
                maxSpend = entry.getValue();
                topCatId = entry.getKey();
            }
        }

        if (topCatId != -1) {
            List<Category> categories = categoryDAO.getCategoriesByUserAndType(userId, "EXPENSE");
            String catName = "Unknown";
            for (Category c : categories) {
                if (c.getId() == topCatId) {
                    catName = c.getName();
                    break;
                }
            }
            insights.add(String.format("Your highest spending category is %s ($%.2f). Consider setting a budget.", catName, maxSpend));
        }
        
        // Savings Rate
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        
        if (totalIncome > 0) {
            double rate = ((totalIncome - totalExpense) / totalIncome) * 100;
            if (rate > 20) {
                insights.add(String.format("Excellent savings rate! You are saving %.1f%% of your income.", rate));
            } else if (rate > 0) {
                insights.add(String.format("You're saving %.1f%% of your income. Aim for at least 20%%.", rate));
            } else {
                insights.add("Warning: You are spending more than you earn. Review your budget.");
            }
        }

        return insights;
    }
}
