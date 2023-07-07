package com.main.personalfinances.daos;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.main.personalfinances.data.BudgetAndSavings;

@Dao
public interface BudgetAndSavingsDao {
    @Transaction
    @Query("SELECT * FROM BUDGET")
    public BudgetAndSavings getSavings(int budgetId);
}
