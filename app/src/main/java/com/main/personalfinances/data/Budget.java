package com.main.personalfinances.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;

    private double startingAmount;

    private double currentAmount;

    public Budget() {

    }
    public Budget(int userId, double startingAmount) {
        this.userId = userId;
        this.startingAmount = startingAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getStartingAmount() {
        return startingAmount;
    }

    public void setStartingAmount(double startingAmount) {
        this.startingAmount = startingAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }
}
