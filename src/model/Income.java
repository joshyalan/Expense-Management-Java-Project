package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * POJO representing an Income record.
 * Demonstrates Inheritance by extending the Transaction abstract class.
 */
public class Income extends Transaction {

    public Income() {
        super();
    }

    public Income(int userId, int accountId, int categoryId, double amount, Date date, String description, String receiptPath) {
        super(userId, accountId, categoryId, amount, date, description, receiptPath);
    }

    public Income(int id, int userId, int accountId, int categoryId, double amount, Date date, String description, String receiptPath, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.receiptPath = receiptPath;
        this.createdAt = createdAt;
    }
}
