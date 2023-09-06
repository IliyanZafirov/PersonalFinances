package com.main.personalfinances.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents savings. Has target and current amount. Savings are related to a budget.
 */
@Entity(tableName = "savings")
public class Savings {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private final long budgetId;
    private double targetAmount;

    private double currentAmount;

    public Savings(long budgetId, double targetAmount) {
        this.budgetId = budgetId;
        this.targetAmount = targetAmount;

    }

    public void addMoney(double moneyToAdd) {
        if(moneyToAdd > 0 ) {
            currentAmount = currentAmount + moneyToAdd;
        }
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
