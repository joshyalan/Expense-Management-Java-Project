<div align="center">
  <img src="https://raw.githubusercontent.com/mgjos/Expense-Tracker-Pro/main/screenshots/logo.png" alt="Expense Tracker Pro Logo" width="150" height="150">

  # Expense Tracker Pro
  
  **Your Personal Financial Command Center**

  [![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
  [![Java: 11+](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://www.oracle.com/java/)
  [![MySQL: 8.0+](https://img.shields.io/badge/MySQL-8.0%2B-blue.svg)](https://www.mysql.com/)
  [![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
  [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)]()
  
  *Take control of your personal finances with AI-driven insights, beautiful dashboards, and robust data management.*
</div>

---

## 📖 About The Project

**Expense Tracker Pro** is a modern, feature-rich desktop application built in Java, designed to help individuals and small businesses manage their financial health. It bridges the gap between complex enterprise financial software and overly simple mobile trackers by offering a professional-grade interface with powerful local data management.

### Why It Was Built
In an era of subscription-based web apps that own your financial data, Expense Tracker Pro was built to give users complete sovereignty over their data. By leveraging a local MySQL database and a secure Java backend, users get enterprise-level analytics and reporting without compromising their privacy.

### Real-World Use Cases
- **Personal Finance:** Track daily expenses, set monthly budgets, and monitor savings goals.
- **Freelancers:** Categorize business versus personal expenses and export detailed PDF/Excel reports for tax season.
- **Small Teams:** Use multiple accounts (wallets, bank accounts, credit cards) to maintain clear cash flow visibility.

---

## ✨ Feature Highlights

| Feature | Description | Screenshot Reference |
| :---: | :--- | :--- |
| 🔐 **Authentication** | Secure BCrypt password hashing and session management. | *See Screenshots section* |
| 📊 **Dashboard** | Dynamic widgets showing total balance, monthly flow, and health score. | `screenshots/dashboard.png` |
| 🤖 **AI Insights** | Algorithmic analysis of spending patterns to detect anomalies. | `screenshots/ai_insights.png` |
| 🎯 **Budgets** | Set and track monthly category budgets with visual progress bars. | `screenshots/budgets.png` |
| 📈 **Charts** | JFreeChart integration for beautiful pie and bar charts of your spending. | `screenshots/charts.png` |
| 🌙 **Dark Mode** | Seamless switching between light and dark themes via FlatLaf. | `screenshots/dark_mode.png` |
| 📄 **PDF/Excel Export** | Generate professional reports using iText and Apache POI. | `screenshots/reports.png` |
| ⚙️ **Settings** | Highly customizable currencies, UI themes, and account security. | `screenshots/settings.png` |

---

## 📸 Screenshots

*(Note: These are placeholder references. Replace with actual images from the `/screenshots` folder).*

<details>
<summary><b>1. Authentication (Login & Register)</b></summary>
<br>
Secure entry points with modern UI design and validation.
<br><br>
<img src="screenshots/login.png" width="400" alt="Login Screen">
<img src="screenshots/register.png" width="400" alt="Register Screen">
</details>

<details>
<summary><b>2. Main Dashboard & Dark Mode</b></summary>
<br>
The central hub for all financial data, featuring dynamic cards and charts. Fully responsive to light/dark themes.
<br><br>
<img src="screenshots/dashboard.png" width="800" alt="Dashboard Light Mode">
<br><br>
<img src="screenshots/dark_mode.png" width="800" alt="Dashboard Dark Mode">
</details>

<details>
<summary><b>3. Budgets & Transactions</b></summary>
<br>
Detailed views for managing granular data.
<br><br>
<img src="screenshots/transactions.png" width="800" alt="Transactions View">
<br><br>
<img src="screenshots/budgets.png" width="800" alt="Budgets View">
</details>

<details>
<summary><b>4. AI Insights & Reports</b></summary>
<br>
Intelligent analytics and exportable reports.
<br><br>
<img src="screenshots/ai_insights.png" width="800" alt="AI Insights">
<br><br>
<img src="screenshots/reports.png" width="800" alt="Reports Module">
</details>

---

## 🛠 Technology Stack

- **Core Language:** Java 11+
- **UI Framework:** Java Swing with [FlatLaf](https://www.formdev.com/flatlaf/) for modern look-and-feel.
- **Database:** MySQL 8.0+
- **Database Connectivity:** JDBC (Data Access Object pattern)
- **Build Tool:** Apache Maven
- **Testing:** JUnit 5 & Mockito
- **Reporting:** iText (PDF), Apache POI (Excel), JFreeChart (Visuals)
- **Security:** jBCrypt (Password Hashing)

---

## 🏗 Architecture Overview

The application follows a clean, decoupled **Model-View-Controller (MVC)** and **N-Tier Architecture**.

1. **Presentation Layer (`src/main/java/view/`)**: Swing UI components utilizing modern layout managers and FlatLaf theming.
2. **Controller Layer (`src/main/java/controller/`)**: Acts as the bridge between the UI and Services.
3. **Business Logic Layer (`src/main/java/service/`)**: Contains complex logic such as AI insights, report generation, and financial health calculations.
4. **Data Access Layer (`src/main/java/dao/`)**: Implements the DAO design pattern to decouple SQL queries from Java code.
5. **Database Layer**: A structured MySQL relational database with enforced foreign keys and cascading deletes.

*Check `docs/architecture.md` for the full visual architecture diagram.*

---

## 🚀 Installation & Setup

### Prerequisites
1. **Java Development Kit (JDK) 11** or higher.
2. **MySQL Server** (8.0+ recommended) and MySQL Workbench.
3. **Apache Maven**.

### 1. Database Setup
1. Open MySQL Workbench.
2. Open the script located at `src/main/java/database/schema.sql`.
3. Execute the entire script to create the `expense_tracker_db` and all necessary tables.

### 2. Application Configuration
1. Open `src/main/java/utils/DBConnection.java`.
2. Update the credentials to match your local MySQL setup:
```java
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
```

### 3. Build and Run
Clone the repository and build via Maven:
```bash
git clone https://github.com/mgjos/Expense-Tracker-Pro.git
cd Expense-Tracker-Pro
mvn clean compile
mvn exec:java -Dexec.mainClass="Main"
```

*Alternatively, open the project folder in IntelliJ IDEA / Eclipse and run `Main.java`.*

---

## 🗺 Roadmap (Future Improvements)

- [ ] **Version 2.1 (Q4 2026):** Cloud Sync capability via AWS S3 for database backups.
- [ ] **Version 2.2:** OCR Receipt Scanning integration.
- [ ] **Version 3.0:** Companion mobile application via RESTful Spring Boot API.

---

## 🤝 Contributing

Contributions make the open-source community an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

Please see our [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting Pull Requests.

---

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

---
<div align="center">
  <b>Built with ❤️ by mgjos</b>
</div>
