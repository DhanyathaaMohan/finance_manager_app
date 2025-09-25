package com.finance_manager.gui;

import com.finance_manager.dao.CategoryDAO;
import com.finance_manager.dao.TransactionDAO;
import com.finance_manager.model.Category;
import com.finance_manager.model.Transaction;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class MainTransactionGUI extends JFrame {

    public MainTransactionGUI() {
        setTitle("Finance Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); 
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton categoryButton = new JButton("CATEGORY");
        JButton transactionButton = new JButton("Transaction");

        categoryButton.setPreferredSize(new Dimension(250, 120));
        transactionButton.setPreferredSize(new Dimension(250, 120));
        categoryButton.setFont(new Font("Arial", Font.BOLD, 20));
        transactionButton.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(categoryButton, gbc);
        gbc.gridx = 1;
        panel.add(transactionButton, gbc);
        add(panel);

        categoryButton.addActionListener(e -> {
            new CategoryGUI().setVisible(true);
        });

        transactionButton.addActionListener(e -> {
            new TransactionGUI().setVisible(true);
        });
    }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                MainTransactionGUI mainGUI = new MainTransactionGUI();
                mainGUI.setVisible(true);
            });
    }
}

class CategoryGUI extends JFrame {

    private JComboBox<String> categoryComboBox;
    private JLabel totalLabel;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private TransactionDAO transactionDAO = new TransactionDAO();

    public CategoryGUI() {
        setTitle("Category Analysis");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        categoryComboBox = new JComboBox<>();
        topPanel.add(new JLabel("Select Category:"));
        topPanel.add(categoryComboBox);

        totalLabel = new JLabel("Total Spent: 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(totalLabel);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Description", "Amount", "Type", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        loadTransactionCategories();

        categoryComboBox.addActionListener(e -> updateCategoryData());
    }

    private void loadTransactionCategories() {
        try {
            List<String> descriptions = transactionDAO.getAllDescriptions();
            categoryComboBox.removeAllItems();
            for (String desc : descriptions) {
                categoryComboBox.addItem(desc);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategoryData() {
        String selected = (String) categoryComboBox.getSelectedItem();
        if (selected == null || selected.isEmpty()) return;

        try {
            double total = transactionDAO.getTotalAmount(selected);
            totalLabel.setText("Total Spent: " + total);

            List<Transaction> transactions = transactionDAO.getTransactionsByDescription(selected);
            tableModel.setRowCount(0);
            for (Transaction t : transactions) {
                Object[] row = { t.getId(), t.getDescription(), t.getAmount(), t.getType(), t.getDate() };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



class TransactionGUI extends JFrame{

    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;
    private JButton refreshButton;
    private JButton incomeButton;
    private JButton expenseButton;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> typeCombo;
    private TransactionDAO transactionDAO = new TransactionDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    public TransactionGUI(){
        setTitle("Transaction Dashboard");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel summaryPanel = new JPanel(new GridLayout(1,3,20,10));
        incomeLabel = new JLabel("Total Income: 0.0");
        expenseLabel = new JLabel("Total Expense: 0.0");
        balanceLabel = new JLabel("Balance: 0.0");
        
        incomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        expenseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
    
        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);

        String[] columns = {"ID", "Description", "Amount", "Date", "Type"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(transactionTable);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        refreshButton = new JButton("Refresh");
        incomeButton = new JButton("Show Income");
        expenseButton = new JButton("Show Expense");

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        descriptionField = new JTextField(30);
        amountField = new JTextField(10);
        typeCombo = new JComboBox<>(new String[]{"Income", "Expense"});

        JButton addTransactionButton = new JButton("Add Transaction");
        JButton deleteTransactionButton = new JButton("Delete Transaction");

        addPanel.add(new JLabel("Description:"));
        addPanel.add(descriptionField);
        addPanel.add(new JLabel("Amount:"));
        addPanel.add(amountField);
        addPanel.add(new JLabel("Type:"));  
        addPanel.add(typeCombo);

        controlPanel.add(refreshButton);
        controlPanel.add(incomeButton);
        controlPanel.add(expenseButton);
        controlPanel.add(addTransactionButton);
        controlPanel.add(deleteTransactionButton);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.add(summaryPanel, BorderLayout.NORTH);
        topWrapper.add(addPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(topWrapper, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        loadDashboardData();

        refreshButton.addActionListener(e -> loadDashboardData());
        incomeButton.addActionListener(e -> loadTransactionsByType("Income"));
        expenseButton.addActionListener(e -> loadTransactionsByType("Expense"));
        addTransactionButton.addActionListener(e -> addTransaction());
        deleteTransactionButton.addActionListener(e -> deleteTransaction());    

    }

    private void loadDashboardData(){
        try{
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            updateTable(transactions);

            double totalIncome = transactionDAO.getMonthlyIncome();
            double totalExpense = transactionDAO.getMonthlyExpense();
            double balance = totalIncome - totalExpense;

            incomeLabel.setText("Total Income: " + totalIncome);
            expenseLabel.setText("Total Expense: " + totalExpense); 
            balanceLabel.setText("Balance: " + balance);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTransactionsByCategory(String category){
        try{
            List<Transaction> transactions = transactionDAO.getTransactionsByCategory(category);
            updateTable(transactions);

            double totalIncome = 0.0;
            for(Transaction t : transactions){
                if(t.getType().equalsIgnoreCase("Income")){
                    totalIncome += t.getAmount();
                }
            }
            JOptionPane.showMessageDialog(this, "Total Expense in "+category+": "+totalIncome, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadTransactionsByType(String type){
        try{
            List<Transaction> transactions = transactionDAO.getTransactionsByType(type);
            updateTable(transactions);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<Transaction> transactions) {
        tableModel.setRowCount(0);
        for(Transaction t : transactions){
            Object[] row = {
                t.getId(),
                t.getDescription(),
                t.getAmount(),
                t.getDate(),
                t.getType()
            };
            tableModel.addRow(row);
        }
    }


    private void addTransaction(){
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if(description.isEmpty() || amountText.isEmpty() || type == null){
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            Transaction transaction = new Transaction(description, amount, LocalDate.now(), type);
            if(transactionDAO.addTransaction(transaction)){
                JOptionPane.showMessageDialog(this, "Transaction added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                descriptionField.setText("");
                amountField.setText("");
                loadDashboardData();
            }   
        else {
            JOptionPane.showMessageDialog(this, "Failed to add transaction", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }   catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(this, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
    }   catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void deleteTransaction(){
        int selectedRow = transactionTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        try{
            if(transactionDAO.deleteTransaction(transactionId)){
                JOptionPane.showMessageDialog(this, "Transaction deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDashboardData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete transaction", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}