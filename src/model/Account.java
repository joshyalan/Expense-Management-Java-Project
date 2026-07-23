package model;

import java.sql.Timestamp;

public class Account {
    private int id;
    private int userId;
    private String name;
    private double balance;
    private String currency;
    private Timestamp createdAt;

    public Account() {}

    public Account(int userId, String name, double balance, String currency) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public Account(int id, int userId, String name, double balance, String currency, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return name + " (" + currency + ")";
    }
}
