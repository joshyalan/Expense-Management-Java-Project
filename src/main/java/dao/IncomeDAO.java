package dao;

import model.Income;
import java.util.List;
import java.sql.Date;

public interface IncomeDAO {
    boolean addIncome(Income income);
    boolean updateIncome(Income income);
    boolean deleteIncome(int incomeId);
    Income getIncomeById(int incomeId);
    List<Income> getAllIncomeByUser(int userId);
    List<Income> getIncomeByDateRange(int userId, Date startDate, Date endDate);
    List<Income> getIncomeByCategory(int userId, int categoryId);
}
