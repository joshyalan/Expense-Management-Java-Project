<div align="center">
  
# 📊 Enterprise Java Expense Tracker

[![Java Version](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)
[![Contributions Welcome](https://img.shields.io/badge/Contributions-Welcome-brightgreen.svg?style=for-the-badge)](CONTRIBUTING.md)

**A professional, robust, and full-stack Desktop Application built with Java Swing, JDBC, and MySQL adhering to MVC Architecture and SOLID principles.**

[Features](#features) • [Screenshots](#screenshots) • [Installation](#installation-guide) • [Architecture](#project-architecture) • [Database](#database-design) • [Contact](#contact)

</div>

---

## 📖 Project Description

The **Expense Tracker** is a comprehensive desktop application designed to help users take control of their personal finances. Built primarily as an academic Project Based Learning (PBL) submission, this application is engineered to enterprise-grade standards, focusing heavily on Object-Oriented Programming (OOP) paradigms, clean code architecture, and a highly responsive user interface.

### ❓ The Problem It Solves
Managing daily expenses, tracking income streams, and staying within monthly budget constraints is challenging when done manually on paper or unstructured spreadsheets. This software provides a unified dashboard to log transactions, visualize spending habits, set budget limits, and generate actionable financial reports.

### 🎯 Target Audience
- **University Evaluators & Professors:** Demonstrates mastery over core Java, JDBC, GUI design, and Database management.
- **Students & Open Source Contributors:** Serves as a reference implementation of the Model-View-Controller (MVC) pattern in Java.
- **Everyday Users:** Anyone looking to effectively track and organize their personal finances locally without relying on cloud subscriptions.

---

## ✨ Features

✅ **User Authentication:** Secure Registration, Login, and Password Hashing.  
✅ **Interactive Dashboard:** High-level summary of total income, expenses, and current balance.  
✅ **Add Expense & Income:** Log transactions with amount, date, dynamic category, and descriptions.  
✅ **Edit & Delete Transactions:** Complete CRUD functionality for all financial records.  
✅ **Dynamic Categories:** Create custom categories tailored to personal spending habits.  
✅ **Budget Management:** Set monthly budget thresholds for specific categories.  
✅ **Budget Alerts:** Notifications when spending approaches or exceeds predefined limits.  
✅ **Analytical Charts:** Visual representation of spending habits using interactive Pie and Bar Charts.  
✅ **Advanced Search & Filter:** Query historical data by date range, category, or amount.  
✅ **Data Export:** Export transaction history and financial reports to **CSV** and **PDF** formats.  
✅ **Profile Management:** Manage user details, update passwords, and handle preferences.  

---

## 📸 Screenshots

> **Note:** Replace placeholders with actual application screenshots during final delivery.

<div align="center">
  <table>
    <tr>
      <td align="center"><b>Login Screen</b><br><img src="docs/images/login.png" alt="Login Screen" width="400"/></td>
      <td align="center"><b>Main Dashboard</b><br><img src="docs/images/dashboard.png" alt="Dashboard" width="400"/></td>
    </tr>
    <tr>
      <td align="center"><b>Add Expense Form</b><br><img src="docs/images/add-expense.png" alt="Add Expense" width="400"/></td>
      <td align="center"><b>Analytical Reports</b><br><img src="docs/images/report.png" alt="Reports" width="400"/></td>
    </tr>
  </table>
</div>

---

## 🏗️ Project Architecture

The application strictly follows the **Model-View-Controller (MVC)** architectural pattern to ensure separation of concerns, high maintainability, and scalability.

- **Model (`src/model`)**: Defines the core entities (POJOs) like `User`, `Expense`, and `Category`. Ensures data encapsulation.
- **View (`src/view`)**: Comprises all graphical user interface components built using Java Swing (Frames, Panels, Dialogs).
- **Controller (`src/controller`)**: Acts as the bridge between the View and the Model. Handles user interactions, updates the UI, and communicates with the Services.
- **DAO (`src/dao`)**: Data Access Objects. Isolates all database operations (SQL queries, `PreparedStatement`s) away from the business logic.
- **Service (`src/service`)**: Contains complex business logic (e.g., calculating budget deficits, authenticating users) mediating between Controllers and DAOs.
- **Database (`src/database`)**: Holds the SQL scripts for schema generation.
- **Utils (`src/utils`)**: Shared utilities like `DBConnection` (Singleton pattern), Session management, and validation helpers.

---

## 📁 Folder Structure

```text
Expense-Management-Java-Project/
│
├── src/
│   ├── controller/      # UI logic and event listeners
│   ├── dao/             # Data Access Objects (CRUD operations)
│   ├── database/        # SQL scripts (schema.sql)
│   ├── model/           # Encapsulated data entities
│   ├── service/         # Business logic layer
│   ├── utils/           # Utilities (DB connection, Session Manager)
│   ├── view/            # GUI forms and panels (Java Swing)
│   └── resources/
│       ├── images/      # Application icons and assets
│       └── reports/     # Generated PDF/CSV output
│
├── docs/                # UML Diagrams, Screenshots, and Manuals
├── lib/                 # External dependencies (.jar files)
└── README.md            # Project documentation
```

---

## 💻 Technologies Used

| Technology / Tool | Purpose | Version / Details |
| :--- | :--- | :--- |
| **Java** | Core Programming Language | Java 11 or higher |
| **Java Swing** | Graphical User Interface | Standard JDK |
| **JDBC** | Database Connectivity API | Standard JDK |
| **MySQL** | Relational Database Management | MySQL 8.0+ |
| **MySQL Connector/J** | MySQL JDBC Driver | `8.0.33` (or similar) |
| **JFreeChart** | Graph and Chart Generation | `1.5.3` |
| **iText / Apache PDFBox** | PDF Report Generation | Standard API |
| **IntelliJ IDEA / Eclipse**| Integrated Development Environment| Latest |
| **Git & GitHub** | Version Control & Hosting | Latest |

---

## 🗄️ Database Design

The relational database (`expense_tracker_db`) enforces referential integrity through foreign keys and cascading operations.

- **`users`**: Manages authentication credentials, emails, and roles.
- **`categories`**: User-specific tags for transactions (e.g., *Food*, *Rent*, *Salary*).
- **`income`**: Records incoming cash flow, linked to `users` and `categories`.
- **`expenses`**: Records outgoing cash flow, linked to `users` and `categories`.
- **`budgets`**: Tracks monthly spending limits mapped to a specific user and category.
- **`notifications`**: Stores system alerts regarding budget limits and milestones.
- **`reports`**: Maintains a history of generated user reports.

---

## 🚀 Installation Guide

Follow these steps to set up the project on your local machine.

### 1. Prerequisites
- **JDK 11+** installed and environment variables configured.
- **MySQL Server** installed and running locally.
- A Java IDE (IntelliJ IDEA, Eclipse, or NetBeans).

### 2. Clone the Repository
```bash
git clone https://github.com/yourusername/Expense-Management-Java-Project.git
cd Expense-Management-Java-Project
```

### 3. Database Configuration
1. Open your MySQL client (e.g., MySQL Workbench).
2. Open the script located at `src/database/schema.sql`.
3. Execute the entire script. This will automatically create the database `expense_tracker_db`, create all tables, and insert default sample data.

### 4. Configure JDBC Connection
Navigate to `src/utils/DBConnection.java` (or equivalent config file) and ensure the credentials match your local MySQL installation:
```java
private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker_db";
private static final String USER = "root";       // Change to your MySQL username
private static final String PASSWORD = "password"; // Change to your MySQL password
```

### 5. Import and Run
1. Open your IDE and select **Open/Import Project**.
2. Select the `Expense-Management-Java-Project` folder.
3. Ensure the **MySQL Connector JAR** (and `JFreeChart`/PDF libraries) are added to your project's Build Path / Dependencies.
4. Locate `src/Main.java` and click **Run**.

---

## ⚙️ Configuration Details

If you need to customize the database setup, you can adjust the following parameters within your application's `DatabaseUtils` or properties file:

- **Database Name**: Default is `expense_tracker_db`.
- **Username**: Change `root` to your configured MySQL user.
- **Password**: Enter your database server password.
- **Port**: Default MySQL port is `3306`. If using XAMPP/WAMP, verify your port.

---

## 🛠️ Usage

1. **Launch the App:** Run the `Main` class.
2. **Register/Login:** Create a new account or log in with the sample user (`john_doe` / `password123`).
3. **Dashboard:** View your current financial snapshot.
4. **Categories:** Navigate to the Categories panel to add custom expense/income tags.
5. **Add Transaction:** Click 'Add Expense' or 'Add Income', fill in the amount, select a date, and assign a category.
6. **Set Budgets:** Go to the Budgets panel to set limits on categories (e.g., maximum $300 on Food this month).
7. **View Reports:** Access the Reports tab to view Pie charts of your spending and export the data to PDF/CSV.

---

## 📐 UML Diagrams

> **Note:** UML diagrams will be generated and placed in the `docs/` folder prior to final academic submission.

*   **ER Diagram**: `docs/diagrams/er_diagram.png`
*   **Use Case Diagram**: `docs/diagrams/use_case.png`
*   **Class Diagram**: `docs/diagrams/class_diagram.png`
*   **Activity Diagram**: `docs/diagrams/activity.png`
*   **Sequence Diagram**: `docs/diagrams/sequence.png`

---

## 🔮 Future Enhancements

While this project is fully functional, potential future improvements include:
- [ ] Integration with a Cloud Database (AWS RDS / Firebase).
- [ ] Dark Mode UI Toggle.
- [ ] Predictive expense analytics using basic Machine Learning (Weka).
- [ ] Recurring expense automation (cron jobs).
- [ ] Multi-currency support and live API exchange rates.

---

## 🧪 Testing

Extensive manual testing has been performed across all application modules:
- **Unit Testing**: Core DAO operations were tested to ensure SQL integrity.
- **Validation Testing**: Forms rigorously check for empty fields, negative numbers, and invalid date formats.
- **Integration Testing**: Ensuring the UI correctly reflects database updates in real-time.

---

## 🚧 Challenges Faced

- **Swing Thread Management**: Ensuring the GUI remained responsive during heavy database calls by utilizing `SwingWorker` threads.
- **Complex SQL Joins**: Displaying comprehensive transaction histories required complex `JOIN` operations between `Users`, `Expenses`, and `Categories` tables.
- **Architecture Enforcement**: Strictly maintaining the MVC separation, ensuring the View never directly accessed the Database.

---

## 📚 Learning Outcomes

Through building this project, the following core concepts were heavily utilized and mastered:
- **Object-Oriented Programming:** Extensive use of Inheritance, Polymorphism, Encapsulation, and Abstraction.
- **Design Patterns:** Implementing the **Singleton** pattern for Database connections and **MVC** for system architecture.
- **Database Normalization & JDBC:** Bridging the gap between a relational database and Java objects seamlessly.
- **Exception Handling:** Building robust try-catch mechanisms to handle SQL exceptions and invalid user inputs gracefully.

---

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 👤 Author

**Student Name / Developer**
- **GitHub:** [@yourusername](https://github.com/yourusername)
- **LinkedIn:** [Your Name](https://linkedin.com/in/yourprofile)
- **Email:** your.email@university.edu

---

## 🙏 Acknowledgements

- Thanks to our University Professors for their guidance and mentorship.
- [Java Swing Oracle Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- The open-source community for providing excellent libraries like JFreeChart.

<div align="center">
  <i>If you found this project helpful, please consider giving it a ⭐!</i>
</div>
