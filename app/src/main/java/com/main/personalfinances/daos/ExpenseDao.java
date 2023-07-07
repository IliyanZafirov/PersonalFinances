package com.main.personalfinances.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.main.personalfinances.data.Expense;
import com.main.personalfinances.data.Savings;

@Dao
public interface ExpenseDao {
    @Insert
    public void insertExpense(Expense expense);
    @Update
    public void updateExpense(Expense expense);
    @Delete
    public void deleteExpense(Expense expense);
}
