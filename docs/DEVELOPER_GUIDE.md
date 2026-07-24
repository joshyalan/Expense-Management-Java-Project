# Developer Guide: Expense Tracker Pro

Welcome to the codebase! This guide covers the architectural design and how to contribute to the codebase effectively.

## Architecture & Patterns

The application is built using Java 11+ and follows a strict **MVC (Model-View-Controller)** / **N-Tier** architecture.

### 1. Data Access Objects (DAO)
We use the DAO pattern (`src/main/java/dao/`) to abstract and encapsulate all access to the data source. 
- **Rule**: Never write SQL queries inside the View or Controller layer. 
- **Rule**: Always use `PreparedStatement` to prevent SQL injection.
- **Connection Pooling**: Connections are managed via `utils.DBConnection`. Always use try-with-resources to ensure connections are closed.

### 2. Business Logic (Services)
The `src/main/java/service/` package holds complex calculations that shouldn't live in the View or DAO.
- Example: `AnalyticsService.java` parses transactions and generates string-based insights. 
- Example: `FinancialHealthCalculator` (if implemented) calculates the 0-100 score.

### 3. Views (Swing & FlatLaf)
UI is located in `src/main/java/view/`. We use [FlatLaf](https://www.formdev.com/flatlaf/) for modern theming.
- **Theme Constraints**: Always use colors from `utils.ui.Theme.java`. Do not hardcode Hex colors in view panels.
- Example: Instead of `new Color(99, 91, 255)`, use `Theme.PRIMARY`. This ensures Dark Mode transitions work flawlessly.

## Setting Up Your Dev Environment

1. Clone the repo and import it into IntelliJ IDEA or Eclipse as a Maven Project.
2. Install MySQL 8.0+.
3. Run `src/main/java/database/schema.sql` in MySQL Workbench to build the tables.
4. Modify `utils/DBConnection.java` to match your local MySQL root password.
5. Build the project: `mvn clean install`.

## Writing Tests

We use **JUnit 5** and **Mockito**.
Tests are located in `src/test/java/`.

When modifying a Service or DAO, please write a corresponding unit test. 
To run tests locally:
```bash
mvn clean test
```

## UI Guidelines for Contributors

- Use `RoundedPanel` for card-like structures.
- Use `BorderLayout` and `BoxLayout` over absolute positioning.
- Ensure all new buttons use the `FlatClientProperties.STYLE` to maintain border radius consistency.
