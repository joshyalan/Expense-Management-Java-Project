# Database Schema Diagram

This document illustrates the Entity-Relationship (ER) model for the MySQL database underpinning Expense Tracker Pro.

```mermaid
erDiagram
    USERS ||--o{ ACCOUNTS : owns
    USERS ||--o{ CATEGORIES : owns
    USERS ||--o{ INCOME : logs
    USERS ||--o{ EXPENSES : logs
    USERS ||--o{ BUDGETS : sets
    USERS ||--o{ SAVINGS_GOALS : creates

    ACCOUNTS ||--o{ INCOME : receives
    ACCOUNTS ||--o{ EXPENSES : funds

    CATEGORIES ||--o{ INCOME : classifies
    CATEGORIES ||--o{ EXPENSES : classifies
    CATEGORIES ||--o{ BUDGETS : bounded_by

    USERS {
        int id PK
        varchar username UK
        varchar password_hash
        varchar email UK
        varchar full_name
        enum role
        timestamp created_at
    }

    ACCOUNTS {
        int id PK
        int user_id FK
        varchar name
        decimal balance
        varchar currency
        timestamp created_at
    }

    CATEGORIES {
        int id PK
        int user_id FK
        varchar name
        enum type "INCOME, EXPENSE"
        varchar icon
        varchar color
        timestamp created_at
    }

    INCOME {
        int id PK
        int user_id FK
        int account_id FK
        int category_id FK
        decimal amount
        date date
        time time
        varchar description
        varchar receipt_path
        timestamp created_at
    }

    EXPENSES {
        int id PK
        int user_id FK
        int account_id FK
        int category_id FK
        decimal amount
        date date
        time time
        varchar description
        varchar receipt_path
        timestamp created_at
    }

    BUDGETS {
        int id PK
        int user_id FK
        int category_id FK
        decimal amount
        int month
        int year
        timestamp created_at
    }
    
    SAVINGS_GOALS {
        int id PK
        int user_id FK
        varchar name
        decimal target_amount
        decimal current_amount
        date target_date
        varchar icon
        varchar color
        timestamp created_at
    }
```

## Indexes and Constraints
- **Foreign Keys**: All foreign keys utilize `ON DELETE CASCADE` to ensure orphaned records (e.g., expenses belonging to a deleted user) are automatically pruned, maintaining referential integrity.
- **Unique Constraints**: Usernames and Emails are strictly unique to prevent authentication conflicts.
