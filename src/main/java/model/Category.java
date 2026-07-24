package model;

import java.sql.Timestamp;

/**
 * POJO representing a Category (Income or Expense).
 */
public class Category {
    private int id;
    private int userId;
    private String name;
    private String type; // 'INCOME' or 'EXPENSE'
    private String icon;
    private String color; // Hex color code
    private Timestamp createdAt;

    // Default Constructor
    public Category() {
    }

    // Parameterized Constructor
    public Category(int id, int userId, String name, String type, String icon, String color, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.color = color;
        this.createdAt = createdAt;
    }

    public Category(int userId, String name, String type) {
        this.userId = userId;
        this.name = name;
        this.type = type;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // Used by JComboBox to display the category name instead of the object memory address
    @Override
    public String toString() {
        return this.name;
    }
}
