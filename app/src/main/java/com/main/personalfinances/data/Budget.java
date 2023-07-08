package com.main.personalfinances.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private double startingAmount;

    private double currentAmount;

    public Budget() {

    }
    public Budget(double startingAmount) {
        this.startingAmount = startingAmount;
        currentAmount = startingAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void pay(double amountToPay) {
        if(amountToPay > 0 && amountToPay < currentAmount) {
            currentAmount = currentAmount - amountToPay;
        }
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }
}
