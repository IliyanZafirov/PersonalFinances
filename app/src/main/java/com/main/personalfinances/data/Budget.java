package com.main.personalfinances.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "budget_table")
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private double amount;

    public Budget (double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount(int id) {
        return this.id == id ? amount:-1;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
