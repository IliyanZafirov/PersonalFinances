package com.main.personalfinances.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.main.personalfinances.enums.TransactionCategory;

import java.util.Date;

@Entity(tableName = "transactions")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int budgetId;
    private TransactionCategory category;
    private Date purchaseDate;
    private Date dueDate;
    private double price;

    public Expense(int budgetId, TransactionCategory category, Date purchaseDate,
                   Date dueDate, double price) {
        this.budgetId = budgetId;
        this.category = category;
        this.purchaseDate = purchaseDate;
        this.dueDate = dueDate;
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

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }
}
