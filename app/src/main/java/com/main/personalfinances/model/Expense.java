package com.main.personalfinances.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.main.personalfinances.enums.TransactionCategory;

import java.time.LocalDate;


/**
 * Represents a real life expense that is related to a budget
 * It has category, description, price and date of payment
 */
@Entity(tableName = "expenses")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final int budgetId;
    private final TransactionCategory category;
    private final String description;
    private final LocalDate dateAdded;
    private final double price;

    public Expense(int budgetId, TransactionCategory category, String description,
                   LocalDate dateAdded, double price) {
        this.budgetId = budgetId;
        this.category = category;
        this.description = description;
        this.dateAdded = dateAdded;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public double getPrice() {
        return price;
    }

    public int getBudgetId() {
        return budgetId;
    }
}
