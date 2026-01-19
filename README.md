Finance Manager App
Overview

The Finance Manager App is a Java-based application designed to help users manage and track their personal finances. It allows users to record income and expenses, categorize transactions, and view summaries that help in understanding spending patterns.

This project is structured as a Maven-based Java application, making it easy to build, run, and extend.

Project Structure

finance_manager_app/
│── pom.xml # Maven configuration file
│── .gitignore # Ignored files and folders
│── src/
│ └── main/
│ └── java/
│ └── com/
│ └── Finance_Manager/
│ ├── Main.java
│ ├── Transaction.java
│ ├── FinanceManager.java
│ └── ... (other supporting classes)
│── target/ # Compiled output (auto-generated) 

How the Code Works (High-Level)
1️⃣ Entry Point

The application starts execution from the Main class.
This class contains the main() method.
It initializes the core finance manager logic and handles user interaction (menu-driven or input-based).

2️⃣ Core Logic

A FinanceManager class manages all financial operations such as:
Adding income
Adding expenses
Viewing transaction history
Calculating total balance

3️⃣ Data Model

A Transaction class represents a financial record.
Each transaction typically contains:
Amount
Type (Income / Expense)
Category
Date / Description

4️⃣ Flow of Execution

User runs the application
Menu options are displayed
User selects an operation
Input is processed
Data is stored in memory (or file, if implemented)
Summary/output is displayed

Features

Add income and expense records
Categorize transactions
View transaction history
Calculate balance
Clean object-oriented design

Tech Stack

Java
Maven
Object-Oriented Programming (OOP)

How to Run the Project
Prerequisites

Java JDK 8 or above
Maven installed

Steps
git clone https://github.com/DhanyathaaMohan/finance_manager_app.git
cd finance_manager_app
mvn clean install
mvn exec:java

Demo / Screenshots


