package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * POJO representing an Expense.
 * Demonstrates Inheritance by extending the Transaction abstract class.
 */
public class Expense extends Transaction {

    public Expense() {
        super();
    }

    public Expense(int userId, int accountId, int categoryId, double amount, Date date, java.sql.Time time, String description, String receiptPath) {
        super(userId, accountId, categoryId, amount, date, time, description, receiptPath);
    }

    public Expense(int id, int userId, int accountId, int categoryId, double amount, Date date, java.sql.Time time, String description, String receiptPath, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.description = description;
        this.receiptPath = receiptPath;
        this.createdAt = createdAt;
    }
}
