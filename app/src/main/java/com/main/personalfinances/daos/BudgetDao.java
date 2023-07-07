package com.main.personalfinances.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

import com.main.personalfinances.data.Budget;

@Dao
public interface BudgetDao {

    @Insert
    void insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);
}
