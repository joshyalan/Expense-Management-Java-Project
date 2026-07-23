package dao;

import model.Goal;
import java.util.List;

public interface GoalDAO {
    boolean addGoal(Goal goal);
    boolean updateGoalCurrentAmount(int goalId, double newAmount);
    boolean deleteGoal(int goalId);
    List<Goal> getGoalsByUser(int userId);
}
