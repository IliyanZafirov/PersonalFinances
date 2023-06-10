package com.main.personalfinances.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "savings_table")
public class Savings {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Double savingsGoal;
    private Double currentSavingsAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getSavingsGoal() {
        return savingsGoal;
    }

    public void setSavingsGoal(Double savingsGoal) {
        this.savingsGoal = savingsGoal;
    }

    public Double getCurrentSavingsAmount() {
        return currentSavingsAmount;
    }

    public void setCurrentSavingsAmount(Double currentSavingsAmount) {
        this.currentSavingsAmount = currentSavingsAmount;
    }
}
