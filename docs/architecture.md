# Expense Tracker Pro Architecture

This document provides a high-level overview of the architectural design of Expense Tracker Pro.

## System Design (N-Tier Architecture)

The application strictly adheres to the Model-View-Controller (MVC) paradigm combined with layered n-tier architecture to ensure high maintainability, testability, and separation of concerns.

```mermaid
graph TD
    %% UI Layer
    subgraph UI [Presentation Layer (View)]
        M[Main / Entry Point] --> LV[Login/Register View]
        LV --> DV[Dashboard View]
        DV --> Panels[Settings, Budgets, Reports Panels]
    end

    %% Controllers
    subgraph Control [Controller Layer]
        AC[Auth Controller]
        UI --> AC
    end

    %% Services
    subgraph Service [Business Logic Layer (Service)]
        TS[Transaction Service]
        AS[Analytics Service]
        BS[Budget Service]
        
        Panels --> TS
        Panels --> AS
        Panels --> BS
    end

    %% Data Access
    subgraph DataAccess [Data Access Layer (DAO)]
        UD[User DAO]
        TD[Transaction DAOs]
        BD[Budget DAO]
        
        AC --> UD
        TS --> TD
        AS --> TD
        BS --> BD
    end

    %% Database
    subgraph DB [Database Layer]
        MySQL[(MySQL Database)]
        
        UD --> MySQL
        TD --> MySQL
        BD --> MySQL
    end

    classDef ui fill:#4F46E5,stroke:#3730A3,stroke-width:2px,color:#fff;
    classDef control fill:#10B981,stroke:#047857,stroke-width:2px,color:#fff;
    classDef service fill:#F59E0B,stroke:#B45309,stroke-width:2px,color:#fff;
    classDef dao fill:#6366F1,stroke:#4338CA,stroke-width:2px,color:#fff;
    classDef db fill:#334155,stroke:#0F172A,stroke-width:2px,color:#fff;
    
    class M,LV,DV,Panels ui;
    class AC control;
    class TS,AS,BS service;
    class UD,TD,BD dao;
    class MySQL db;
```

## Directory Structure Responsibilities

- **`/view`**: Contains all Swing classes. Responsible purely for rendering state and capturing user input.
- **`/controller`**: Handles routing and bridging complex interactions between views and services.
- **`/service`**: Contains business logic, calculations (e.g., Financial Health Score, AI Anomaly detection), and validation.
- **`/model`**: Plain Old Java Objects (POJOs) representing database entities.
- **`/dao`**: Interfaces and Implementations for executing JDBC SQL queries safely.
- **`/utils`**: Helpers such as `DBConnection`, `SessionManager`, and `Theme` styling singletons.
