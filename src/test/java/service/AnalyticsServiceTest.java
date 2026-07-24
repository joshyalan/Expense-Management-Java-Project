package service;

import dao.CategoryDAOImpl;
import dao.ExpenseDAOImpl;
import dao.IncomeDAOImpl;
import model.Category;
import model.Expense;
import model.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {

    @Mock
    private ExpenseDAOImpl expenseDAO;

    @Mock
    private IncomeDAOImpl incomeDAO;

    @Mock
    private CategoryDAOImpl categoryDAO;

    @InjectMocks
    private AnalyticsService analyticsService;

    private int testUserId = 1;

    @BeforeEach
    void setUp() {
        // Mock setups can be done in individual tests based on requirements
    }

    @Test
    void testGenerateInsights_NoExpenses() {
        when(expenseDAO.getAllExpensesByUser(testUserId)).thenReturn(Collections.emptyList());
        when(incomeDAO.getAllIncomeByUser(testUserId)).thenReturn(Collections.emptyList());

        List<String> insights = analyticsService.generateInsights(testUserId);

        assertEquals(1, insights.size());
        assertEquals("Add some expenses to get AI spending insights.", insights.get(0));
    }

    @Test
    void testGenerateInsights_WithData() {
        Date today = Date.valueOf(LocalDate.now());
        Time time = new Time(System.currentTimeMillis());

        Expense e1 = new Expense(1, testUserId, 1, 10, 150.0, today, time, "Groceries", null, null);
        Expense e2 = new Expense(2, testUserId, 1, 10, 50.0, today, time, "Snacks", null, null);
        Income i1 = new Income(1, testUserId, 1, 20, 1000.0, today, time, "Salary", null, null);

        Category c1 = new Category(10, testUserId, "Food", "EXPENSE", "🍔", "#FF0000", null);

        when(expenseDAO.getAllExpensesByUser(testUserId)).thenReturn(Arrays.asList(e1, e2));
        when(incomeDAO.getAllIncomeByUser(testUserId)).thenReturn(Collections.singletonList(i1));
        when(categoryDAO.getCategoriesByUserAndType(testUserId, "EXPENSE")).thenReturn(Collections.singletonList(c1));

        List<String> insights = analyticsService.generateInsights(testUserId);

        // We expect:
        // 1. Comparison with last month (since last month is 0, we saved money) -> Wait, if last month is 0, the code says `if (lastMonthSpend > 0)`. So it skips it.
        // 2. Top Category -> Food ($200.00)
        // 3. Savings Rate -> (1000 - 200)/1000 = 80%. "Excellent savings rate..."

        assertTrue(insights.stream().anyMatch(s -> s.contains("Your highest spending category is Food ($200.00)")));
        assertTrue(insights.stream().anyMatch(s -> s.contains("Excellent savings rate! You are saving 80.0% of your income.")));
    }
}
