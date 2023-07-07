package com.main.personalfinances.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "savings")
public class Savings {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long budgetId;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double targetAmount;

    private double currentAmount;

    public Savings() {

    }
    public Savings(long budgetId, double targetAmount) {
        this.budgetId = budgetId;
        this.targetAmount = targetAmount;

    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }


    public long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }
}
