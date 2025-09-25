package com.finance_manager.model;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private String description;
    private double amount;
    private LocalDate date;
    private Category category;
    private String type;

    public Transaction(String description, double amount, LocalDate date, Category category, String type) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = null;
        this.type = type;
    }
    public Transaction(String description, double amount, LocalDate date, String type) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public Transaction(int id, String description, double amount, LocalDate date, Category category, String type) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
