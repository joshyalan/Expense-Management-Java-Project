-- ====================================================
-- EXPENSE TRACKER DATABASE SCHEMA
-- ====================================================
-- Database: expense_tracker_db
-- Dialect: MySQL
-- ====================================================

-- 1. Create Database and Use It
CREATE DATABASE IF NOT EXISTS expense_tracker_db;
USE expense_tracker_db;

-- 2. Drop existing tables if they exist to prevent errors on re-run
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS income;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- 3. Create Tables

-- USERS Table
-- Stores user authentication and profile details
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CATEGORIES Table
-- Stores income and expense categories. Linked to a user for customization.
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- INCOME Table
-- Stores user income records
CREATE TABLE income (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- EXPENSES Table
-- Stores user expense records
CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- BUDGETS Table
-- Stores monthly budget limits per category for a user
CREATE TABLE budgets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- NOTIFICATIONS Table
-- Stores system alerts (e.g., budget exceeded warnings)
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- REPORTS Table
-- Stores metadata of generated reports for history tracking
CREATE TABLE reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    report_type ENUM('MONTHLY', 'YEARLY', 'CUSTOM') NOT NULL,
    file_path VARCHAR(255),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ====================================================
-- 4. Insert Sample Data
-- ====================================================

-- Note: Passwords should be hashed in production using BCrypt. 
-- For demo purposes, we will assume 'password123' is hashed as 'hashed_password_demo'.
INSERT INTO users (username, password_hash, email, full_name, role) VALUES 
('john_doe', 'hashed_password_demo', 'john@example.com', 'John Doe', 'USER'),
('admin_user', 'hashed_password_admin', 'admin@example.com', 'Admin User', 'ADMIN');

INSERT INTO categories (user_id, name, type) VALUES 
(1, 'Salary', 'INCOME'),
(1, 'Freelance', 'INCOME'),
(1, 'Food & Dining', 'EXPENSE'),
(1, 'Rent', 'EXPENSE'),
(1, 'Utilities', 'EXPENSE');

INSERT INTO income (user_id, category_id, amount, date, description) VALUES 
(1, 1, 5000.00, '2023-10-01', 'October Salary'),
(1, 2, 850.00, '2023-10-15', 'Web Design Project');

INSERT INTO expenses (user_id, category_id, amount, date, description) VALUES 
(1, 4, 1200.00, '2023-10-02', 'October Rent'),
(1, 3, 45.50, '2023-10-05', 'Groceries'),
(1, 5, 150.00, '2023-10-10', 'Electric Bill');

INSERT INTO budgets (user_id, category_id, amount, month, year) VALUES 
(1, 3, 400.00, 10, 2023);

INSERT INTO notifications (user_id, message) VALUES 
(1, 'Welcome to the Expense Tracker! Set up your first budget today.');
