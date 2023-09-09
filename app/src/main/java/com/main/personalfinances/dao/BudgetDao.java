package com.main.personalfinances.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.main.personalfinances.model.Budget;

@Dao
public interface BudgetDao {
    @Insert
    long insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Query("SELECT * FROM budget LIMIT 1")
    Budget getBudget();
}
