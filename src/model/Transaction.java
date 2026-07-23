package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Abstract class representing a Financial Transaction.
 * Demonstrates the OOP concept of Abstraction and Inheritance.
 * Both Income and Expense classes will inherit from this.
 */
public abstract class Transaction {
    protected int id;
    protected int userId;
    protected int categoryId;
    protected int accountId;
    protected double amount;
    protected Date date;
    protected String description;
    protected String receiptPath;
    protected Timestamp createdAt;

    public Transaction() {
    }

    public Transaction(int userId, int accountId, int categoryId, double amount, Date date, String description, String receiptPath) {
        this.userId = userId;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.receiptPath = receiptPath;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getReceiptPath() { return receiptPath; }
    public void setReceiptPath(String receiptPath) { this.receiptPath = receiptPath; }
}
