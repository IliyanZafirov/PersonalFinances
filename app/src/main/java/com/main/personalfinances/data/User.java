package com.main.personalfinances.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "user_table")
public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

//    public Budget getBudget() {
//        return budget;
//    }
//
//    public void setBudget(Budget budget) {
//        this.budget = budget;
//    }

    public int getSavingsId() {
        return savingsId;
    }

    public void setSavingsId(int savingsId) {
        this.savingsId = savingsId;
    }

//    public Savings getSavings() {
//        return savings;
//    }
//
//    public void setSavings(Savings savings) {
//        this.savings = savings;
//    }

//    public List<Transaction> getTransactions() {
//        return transactions;
//    }
//
//    public void setTransactions(List<Transaction> transactions) {
//        this.transactions = transactions;
//    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int budgetId;

    //@Relation(parentColumn = "budgetId", entityColumn = "id", entity = Budget.class)
    //private Budget budget;

    private int savingsId;

    //@Relation(parentColumn = "savingsId", entityColumn = "id", entity = Savings.class)
    //private Savings savings;

    //@Relation(parentColumn = "id", entityColumn = "userId", entity = Transaction.class)
    //private List<Transaction> transactions;

    public User(String name, int budgetId, int savingsId) {
        this.name = name;
        this.budgetId = budgetId;
        this.savingsId = savingsId;
    }

    // Getters and setters

    @Override
    public String toString() {
        return this.name;
    }
}
