package com.main.personalfinances.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.main.personalfinances.data.Expense;
import com.main.personalfinances.data.Savings;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    public void insertExpense(Expense expense);
    @Update
    public void updateExpense(Expense expense);
    @Delete
    public void deleteExpense(Expense expense);

    @Query("SELECT * FROM expenses")
    public LiveData<List<Expense>> getAllExpenses();

    @Query("DELETE FROM expenses")
    public void deleteAllExpenses();
}
