# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- Cloud Sync (AWS S3 backup).
- Companion mobile application via RESTful Spring Boot API.
- OCR receipt scanning support.

---

## [2.0.0] - 2026-07-24

### Added
- **Premium UI Transformation:** Implemented FlatLaf themes including dynamic Dark Mode support and rounded corner aesthetics.
- **Dashboard:** Dynamic tracking of total balance, monthly income, expenses, and health score calculations.
- **AI Insights:** Automated detection of spending anomalies and personalized financial insights via `AnalyticsService`.
- **Budgets Module:** Added visual tracking of monthly budget limits against actual category spending.
- **Reporting:** Introduced PDF and Excel export generation functionality.
- **Charts:** Added JFreeChart visual metrics for expense categorization and trend analysis.
- **Security:** Added BCrypt password hashing for user authentication and "Change Password" settings module.
- **Testing:** Introduced JUnit 5 framework and initial unit testing suite.
- **Project Structure:** Fully migrated source code to standard Maven layout (`src/main/java` and `src/test/java`).

### Changed
- Refactored `DashboardView.java` to fetch real-time data instead of using mock strings.
- Refactored database initialization process to handle foreign-key constraints robustly during development resets.
- Updated database connection utility (`DBConnection.java`) for better connection pooling practices.

### Fixed
- Fixed an issue where the `categories` table was not properly generated due to foreign-key conflicts on legacy tables (`goals`).
- Fixed text truncation inside Dashboard summary cards by resizing the `fontHero` from `42` to `32`.
- Fixed layout stretching bug on the Default Currency selector inside `SettingsPanel`.

---

## [1.0.0] - 2026-05-15

### Added
- Initial release.
- Basic CRUD operations for Accounts, Income, and Expenses.
- Simple Swing UI layout.
- MySQL database schema v1.0.
