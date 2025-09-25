package com.finance_manager.dao;

import com.finance_manager.util.DatabaseConnection;
import com.finance_manager.model.Transaction;
import com.finance_manager.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDate;



public class TransactionDAO{
    private static final String LOAD_TRANSACTIONS = "SELECT t.id,t.description,t.amount,t.date,c.id AS category_id ,c.name AS category_name, t.type FROM transaction t LEFT JOIN category c ON t.category_id = c.id ORDER BY t.date DESC";
    private static final String LOAD_MONTHLY_INCOME = "SELECT IFNULL(SUM(amount),0) AS total_income FROM transaction WHERE type = 'Income' AND MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE())"; 
    private static final String LOAD_MONTHLY_EXPENSE = "SELECT IFNULL(SUM(amount),0) AS total_expense FROM transaction WHERE type = 'Expense' AND MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE())";
    private static final String LOAD_BY_CATEGORY = "SELECT t.id,t.description,t.amount,t.date,c.name AS category_name, c.id AS category_id , t.type FROM transaction t LEFT JOIN category c ON t.category_id = c.id WHERE c.name = ? ORDER BY t.date DESC";
    private static final String LOAD_BY_TYPE = "SELECT t.id,t.description,t.amount,t.date,c.name AS category_name ,c.id AS category_id,t.type FROM transaction t LEFT JOIN category c ON t.category_id = c.id WHERE t.type = ? ORDER BY t.date DESC";
    private static final String ADD_TRANSACTION = "INSERT INTO transaction(description, amount, date, category_id, type) VALUES(?,?,?,?,?)";
    private static final String DELETE_TRANSACTION = "DELETE FROM transaction WHERE id = ?";
    private static final String LOAD_ALL_TRANSACTION = "SELECT DISTINCT description FROM transaction";
    private static final String TOTAL_AMOUNT_TRANSACTION = "SELECT SUM(amount) AS total FROM transaction WHERE description = ?";
    private static final String LOAD_TRANSACTION_DESCRIPTION = "SELECT t.id, t.description, t.amount, t.date, c.name AS category_name, c.id AS category_id, t.type FROM transaction t LEFT JOIN category c ON t.category_id = c.id WHERE t.description = ? ORDER BY t.date DESC";



    public List<String> getAllDescriptions() throws SQLException {
        List<String> descriptions = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_ALL_TRANSACTION);
            ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()) {
                String desc = rs.getString("description");
                if(desc != null && !desc.isEmpty())
                    descriptions.add(desc);
            }
        }
        return descriptions;
    }

    public double getTotalAmount(String description) throws SQLException {
        if(description == null || description.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(TOTAL_AMOUNT_TRANSACTION))
        {
            stmt.setString(1, description);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        }
        return total;
    }

    public List<Transaction> getTransactionsByDescription(String description) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_TRANSACTION_DESCRIPTION))
        {
            stmt.setString(1, description);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(getTransactionRow(rs));
                }
            }
        }
        return transactions;
    }


    public Transaction getTransactionRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String description = rs.getString("description");
        double amount = rs.getDouble("amount");
        Date sqlDate = rs.getDate("date");
        LocalDate date = sqlDate !=null ? sqlDate.toLocalDate() : null;
        int categoryId = rs.getInt("category_id");
        String categoryName = rs.getString("category_name");
        Category category = categoryId != 0 ? new Category(categoryId, categoryName) : null;
        String type = rs.getString("type");
        return new Transaction(id, description, amount, date, category, type);  
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_TRANSACTIONS);
            ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()) {
                transactions.add(getTransactionRow(rs));
            }
        }
        return transactions;
    }

    public double getMonthlyIncome() throws SQLException {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_MONTHLY_INCOME);
            ResultSet rs = stmt.executeQuery())
        {
            if (rs.next()) {
                return rs.getDouble("total_income");
            }
            return 0.0;
        }
    }

    public double getMonthlyExpense() throws SQLException {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_MONTHLY_EXPENSE);
            ResultSet rs = stmt.executeQuery())
        {
            if (rs.next()) {
                return rs.getDouble("total_expense");
            }
            return 0.0;
        }
    }

    public List<Transaction> getTransactionsByCategory(String category) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_BY_CATEGORY))
        {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(getTransactionRow(rs));
                }
            }
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByType(String type) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOAD_BY_TYPE))
        {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(getTransactionRow(rs));
                }
            }
        }
        return transactions;
    }

    public boolean addTransaction(Transaction transaction) throws SQLException {
        int row = 0;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(ADD_TRANSACTION)) 
            {
            stmt.setString(1, transaction.getDescription());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setDate(3, Date.valueOf(transaction.getDate()));
            if(transaction.getCategory() != null){
                stmt.setInt(4, transaction.getCategory().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setString(5, transaction.getType());
            row = stmt.executeUpdate();
            }
        return row > 0;
    }

    public boolean deleteTransaction(int transactionId) throws SQLException {
        int row =0;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_TRANSACTION))
        {
            stmt.setInt(1, transactionId);
            row = stmt.executeUpdate();
        }
        return row > 0;
    }

    





}                       